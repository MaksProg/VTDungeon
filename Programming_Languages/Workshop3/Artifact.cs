using System;

public class HealthPotion : IArtifact
{
    private int healAmount;
    public HealthPotion(int heal) { healAmount = heal; }

    public void Use(Hero self, Hero target)
    {
        self.HP += healAmount;
        ConsoleUtils.Log($"{self.Name} использует HealthPotion и восстанавливает {healAmount} HP (текущее HP: {self.HP})", ConsoleColor.Yellow);
    }
}

public class DamageArtifact : IArtifact
{
    private int addDamage;
    private int duration;

    public DamageArtifact(int addDamage, int duration = 1)
    {
        this.addDamage = addDamage;
        this.duration = duration;
    }

    public void Use(Hero self, Hero target)
    {
        self.TempAttackBuff += addDamage;
        self.TempAttackBuffTurnsLeft = Math.Max(self.TempAttackBuffTurnsLeft, duration);
        ConsoleUtils.Log($"{self.Name} использует DamageArtifact: +{addDamage} к атаке на {duration} ход(а).", ConsoleColor.Yellow);
    }
}
