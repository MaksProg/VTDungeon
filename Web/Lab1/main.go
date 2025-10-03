package main

import (
	"encoding/json"
	"log"
	"math"
	"net"
	"net/http"
	"strconv"
	"time"
)

type Result struct {
	X      float64 `json:"x"`
	Y      float64 `json:"y"`
	R      float64 `json:"r"`
	Hit    bool    `json:"hit"`
	Time   string  `json:"time"`
	ExecMs int     `json:"ms_exec"`
}

type Response struct {
	ReceivedAt string   `json:"received_at"`
	Result     []Result `json:"result"`
}

func hit(x, y, r float64) bool {
	if r <= 0 || math.IsNaN(r) || math.IsInf(r, 0) {
		return false
	}
	if math.IsNaN(x) || math.IsInf(x, 0) || math.IsNaN(y) || math.IsInf(y, 0) {
		return false
	}

	if x <= 0 && y >= 0 && x*x+y*y <= r*r+1e-9 {
		return true
	}
	if x >= 0 && y >= 0 && y <= (-x/2.0+r/2.0)+1e-9 {
		return true
	}
	if x >= 0 && x <= r && y <= -r/2.0+1e-9 && y >= -r-1e-9 {
		return true
	}
	return false
}

func loadHistory(r *http.Request) []Result {
	c, err := r.Cookie("history")
	if err != nil {
		return nil
	}
	var hist []Result
	_ = json.Unmarshal([]byte(c.Value), &hist)
	return hist
}

func saveHistory(w http.ResponseWriter, hist []Result) {
	data, _ := json.Marshal(hist)
	http.SetCookie(w, &http.Cookie{
		Name:     "history",
		Value:    string(data),
		Path:     "/",
		HttpOnly: false, 
		Expires:  time.Now().Add(24 * time.Hour),
	})
}

func handler(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	w.Header().Set("Access-Control-Allow-Origin", "http://127.0.0.1:5500")
	w.Header().Set("Access-Control-Allow-Credentials", "true")
	w.Header().Set("Content-Type", "application/json; charset=utf-8")

	if r.Method != http.MethodGet {
		http.Error(w, "Only GET allowed", http.StatusMethodNotAllowed)
		return
	}

	q := r.URL.Query()
	xStr, yStr, rStr := q.Get("x"), q.Get("y"), q.Get("r")

	x, errX := strconv.ParseFloat(xStr, 64)
	y, errY := strconv.ParseFloat(yStr, 64)
	radius, errR := strconv.ParseFloat(rStr, 64)

	if errX != nil || errY != nil || errR != nil || !isFinite(x) || !isFinite(y) || !isFinite(radius) || radius <= 0 {
		http.Error(w, "Invalid params: x, y must be numbers; r must be positive", http.StatusBadRequest)
		return
	}

	res := Result{
		X:      x,
		Y:      y,
		R:      radius,
		Hit:    hit(x, y, radius),
		Time:   time.Now().Format("15:04:05"),
		ExecMs: int(time.Since(start).Milliseconds()),
	}

	history := loadHistory(r)
	history = append(history, res)
	if len(history) > 1000 {
		history = history[len(history)-1000:]
	}

	saveHistory(w, history)

	resp := Response{
		ReceivedAt: time.Now().Format(time.RFC3339),
		Result:     history,
	}

	_ = json.NewEncoder(w).Encode(resp)
}

func resetHandler(w http.ResponseWriter, r *http.Request) {
	http.SetCookie(w, &http.Cookie{
		Name:     "history",
		Value:    "",
		Path:     "/",
		HttpOnly: true,
		Expires:  time.Unix(0, 0),
	})
	w.Write([]byte(`{"status":"reset done"}`))
}

func isFinite(v float64) bool {
	return !math.IsNaN(v) && !math.IsInf(v, 0)
}

func main() {
	mux := http.NewServeMux()
	mux.HandleFunc("/api", handler)
	mux.HandleFunc("/reset", resetHandler)

	fs := http.FileServer(http.Dir("./www"))
	mux.Handle("/", fs)

	addr := ":9000"
	l, err := net.Listen("tcp", addr)
	if err != nil {
		log.Fatalf("listen: %v", err)
	}
	log.Printf("HTTP server listening on %s", addr)
	log.Fatal(http.Serve(l, mux))
}
