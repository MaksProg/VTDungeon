public interface IArtifact
{
    void Use(Hero self, Hero target);
}
public interface IAttackable
{
    void TakeDamage(int damage);
}


public abstract class Hero : IAttackable
{
    public string Name { get; set; }
    public int HP { get; set; }
    public int Defense { get; set; }
    public int Attack { get; set; }

    public int TempAttackBuff { get; set; } = 0;
    public int TempAttackBuffTurnsLeft { get; set; } = 0;

  
    public IArtifact? Artifact { get; set; }

    public Hero(string name, int attack, int hp, int defense)
    {
        Name = name;
        Attack = attack;
        HP = hp;
        Defense = defense;
    }

    public void TakeDamage(int damage)
    {
        if (damage <= 0) return;

        int realDamage = damage - Defense;
        if (realDamage < 0) realDamage = 0;

        HP -= realDamage;
        if (HP < 0) HP = 0;
    }

    public bool IsAlive() => HP > 0;

    public virtual void TickBuffs()
    {
        if (TempAttackBuffTurnsLeft > 0)
        {
            TempAttackBuffTurnsLeft--;
            if (TempAttackBuffTurnsLeft == 0)
            {
                ConsoleUtils.Log($"{Name} теряет бонус к атаке (+{TempAttackBuff}).", ConsoleColor.DarkGray);
                TempAttackBuff = 0;
            }
        }
    }

    public int GetCurrentAttack() => Attack + TempAttackBuff;

    public void UseArtifact(Hero target)
    {
        if (Artifact != null)
        {
            Artifact.Use(this, target);
            Artifact = null; 
        }
    }

    public abstract void SpecialAbility(Hero target);

    public virtual void Method() => Console.WriteLine("Hero");

    public override string ToString()
    {
        return $"[Name = {Name}, HP = {HP}, Attack = {Attack}, Defense = {Defense}]";
    }
}
