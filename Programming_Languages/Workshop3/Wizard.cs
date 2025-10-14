using System;

public class Wizard : Hero
{
    public Wizard(string name, int attack, int hp, int defense)
        : base(name, attack, hp, defense)
    {
    }

    public override void SpecialAbility(Hero target)
    {
        Console.WriteLine($"{Name} применяет заклинание и игнорирует защиту противника!");
        int damage = Attack;
        target.HP -= damage;
        if (target.HP < 0) target.HP = 0;
    }

    public override void Method()
    {
        Console.WriteLine("Wizard");
    }
}
