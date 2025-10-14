using System;

public class Warrior : Hero
{
    public Warrior(string name, int attack, int hp, int defense)
        : base(name, attack, hp, defense)
    {
    }

    public override void Method()
    {
        Console.WriteLine("Warrior");
    }

    public override void SpecialAbility(Hero target)
    {
        Console.WriteLine($"{Name} использует мощный удар (x2 урон)!");
        target.TakeDamage(Attack * 2);
    }
}
