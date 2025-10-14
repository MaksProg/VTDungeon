using System;
using System.Text;
using System.Globalization;

// Устанавливаем кодировку UTF-8, чтобы кириллица отображалась корректно
Console.OutputEncoding = Encoding.UTF8;
Console.InputEncoding = Encoding.UTF8;

// Устанавливаем русскую культуру (например для дат, чисел)
CultureInfo.CurrentCulture = new CultureInfo("ru-RU");
CultureInfo.CurrentUICulture = new CultureInfo("ru-RU");

ConsoleUtils.Log("=== Автобаттлер запущен ===", ConsoleColor.Magenta);

Hero hero1 = new Warrior("Воин", attack: 10, hp: 40, defense: 4);
Hero hero2 = new Wizard("Маг", attack: 12, hp: 35, defense: 2);
hero1.Artifact = new HealthPotion(15);    
hero2.Artifact = new DamageArtifact(5, 2);

ConsoleUtils.Log($"Созданы герои:\n{hero1}\n{hero2}", ConsoleColor.DarkYellow);

AutoBattler.StartBattle(hero1, hero2);

ConsoleUtils.Log("=== Бой окончен ===", ConsoleColor.Magenta);
