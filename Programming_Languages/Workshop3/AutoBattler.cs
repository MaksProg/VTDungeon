public static class AutoBattler
{
    public static void StartBattle(Hero hero1, Hero hero2)
    {
        ConsoleUtils.ClearLog();

        var random = new Random();
        int round = 1;

        ConsoleUtils.Log($"Начало битвы: {hero1.Name} vs {hero2.Name}", ConsoleColor.Yellow);

        while (hero1.IsAlive() && hero2.IsAlive())
        {
            ConsoleUtils.Log($"\nРаунд {round}", ConsoleColor.Cyan);

            Hero attacker = random.Next(0, 2) == 0 ? hero1 : hero2;
            Hero defender = attacker == hero1 ? hero2 : hero1;

            // Каждый 3-й ход — спецспособность
            if (round % 3 == 0)
            {
                attacker.SpecialAbility(defender);
            }
            // Каждый 5-й ход — артефакт
            else if (round % 5 == 0)
            {
                attacker.UseArtifact(defender);
            }
            else
            {
                int attackValue = attacker.GetCurrentAttack();
                defender.TakeDamage(attackValue);
                ConsoleUtils.Log($"{attacker.Name} атакует {defender.Name} на {attackValue} урона!", ConsoleColor.White);
            }

            hero1.TickBuffs();
            hero2.TickBuffs();

            ConsoleUtils.Log($"{hero1.Name}: {hero1.HP} HP | {hero2.Name}: {hero2.HP} HP", ConsoleColor.Gray);

            if (!defender.IsAlive())
            {
                ConsoleUtils.Log($"{attacker.Name} ПОБЕЖДАЕТ!", ConsoleColor.Green);
                Console.Beep();
                break;
            }

            round++;
        }

        ConsoleUtils.Log("=== Битва окончена ===", ConsoleColor.Yellow);
    }
}
