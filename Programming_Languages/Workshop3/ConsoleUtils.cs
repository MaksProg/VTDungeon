using System;
using System.IO;

public static class ConsoleUtils
{
    private static readonly string logFile = Path.Combine(Environment.CurrentDirectory, "log.txt");

    public static void Log(string message, ConsoleColor color = ConsoleColor.White)
    {
        Console.ForegroundColor = color;
        Console.WriteLine(message);
        Console.ResetColor();

        try
        {
            File.AppendAllText(logFile, message + Environment.NewLine, System.Text.Encoding.UTF8);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка записи в лог: {ex.Message}");
        }
    }

    public static void ClearLog()
    {
        try
        {
            File.WriteAllText(logFile, string.Empty, System.Text.Encoding.UTF8);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка очистки лога: {ex.Message}");
        }
    }
}
