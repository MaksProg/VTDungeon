from sympy import symbols, Eq, solve

# Задаем вектор для проверки
test_vector = [0.5, 1, -2, 0]  # Добавляем нулевую компоненту, чтобы размер совпадал

# Символы для коэффициентов линейной комбинации
a1, a2, a3 = symbols('a1 a2 a3')

# Проверка: может ли test_vector быть комбинацией v1, v2, v3
eq1 = Eq(a1 * v1[0] + a2 * v2[0] + a3 * v3[0], test_vector[0])
eq2 = Eq(a1 * v1[1] + a2 * v2[1] + a3 * v3[1], test_vector[1])
eq3 = Eq(a1 * v1[2] + a2 * v2[2] + a3 * v3[2], test_vector[2])
eq4 = Eq(a1 * v1[3] + a2 * v2[3] + a3 * v3[3], test_vector[3])

# Решаем систему уравнений
solution_L1 = solve([eq1, eq2, eq3, eq4], (a1, a2, a3))

# Проверка: может ли test_vector быть комбинацией u1, u2
b1, b2 = symbols('b1 b2')
eq5 = Eq(b1 * u1[0] + b2 * u2[0], test_vector[0])
eq6 = Eq(b1 * u1[1] + b2 * u2[1], test_vector[1])
eq7 = Eq(b1 * u1[2] + b2 * u2[2], test_vector[2])
eq8 = Eq(b1 * u1[3] + b2 * u2[3], test_vector[3])

solution_L2 = solve([eq5, eq6, eq7, eq8], (b1, b2))

solution_L1, solution_L2