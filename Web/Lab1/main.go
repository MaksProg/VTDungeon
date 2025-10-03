package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"sync"
	"time"

	"github.com/google/uuid"
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

var (
	mu        sync.Mutex
	histories = make(map[string][]Result) 
)

func hit(x, y, r float64) bool {
	if x <= 0 && y >= 0 && x*x+y*y <= r*r+1e-9 {
		return true
	}
	if x >= 0 && y >= 0 && y <= (-x/2.0+r/2.0)+1e-9 {
		return true
	}
	if x >= 0 && x <= r && y <= 0 && y >= -r {
		return true
	}
	return false
}

func getClientID(w http.ResponseWriter, r *http.Request) string {
	c, err := r.Cookie("client_id")
	if err == nil && c.Value != "" {
		return c.Value
	}
	id := uuid.New().String()
	http.SetCookie(w, &http.Cookie{
		Name:    "client_id",
		Value:   id,
		Path:    "/",
		Expires: time.Now().Add(7 * 24 * time.Hour),
	})
	return id
}

func apiHandler(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json; charset=utf-8")

	if r.Method != http.MethodGet {
		http.Error(w, "Only GET allowed", http.StatusMethodNotAllowed)
		return
	}

	q := r.URL.Query()
	x, errX := strconv.ParseFloat(q.Get("x"), 64)
	y, errY := strconv.ParseFloat(q.Get("y"), 64)
	radius, errR := strconv.ParseFloat(q.Get("r"), 64)

	if errX != nil || errY != nil || errR != nil || radius <= 0 {
		http.Error(w, "Invalid params", http.StatusBadRequest)
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

	clientID := getClientID(w, r)

	mu.Lock()
	histories[clientID] = append(histories[clientID], res)
	if len(histories[clientID]) > 1000 {
		histories[clientID] = histories[clientID][len(histories[clientID])-1000:]
	}
	resp := Response{
		ReceivedAt: time.Now().Format(time.RFC3339),
		Result:     append([]Result(nil), histories[clientID]...),
	}
	mu.Unlock()

	json.NewEncoder(w).Encode(resp)
}

func historyHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json; charset=utf-8")

	if r.Method != http.MethodGet {
		http.Error(w, "Only GET allowed", http.StatusMethodNotAllowed)
		return
	}

	clientID := getClientID(w, r)

	mu.Lock()
	hist := histories[clientID]
	resp := Response{
		ReceivedAt: time.Now().Format(time.RFC3339),
		Result:     append([]Result(nil), hist...),
	}
	mu.Unlock()

	json.NewEncoder(w).Encode(resp)
}

func main() {
	mux := http.NewServeMux()
	mux.HandleFunc("/api", apiHandler)
	mux.HandleFunc("/history", historyHandler)

	fs := http.FileServer(http.Dir("./www"))
	mux.Handle("/", fs)

	addr := ":9000"
	log.Printf("Server listening on %s", addr)
	log.Fatal(http.ListenAndServe(addr, mux))
}
