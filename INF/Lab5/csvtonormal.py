import pandas as pd

# Задайте имя входного и выходного файла
input_file = 'exchange.csv'  # замените на имя вашего входного файла
output_file = 'exchangenorm.csv'  # имя выходного файла

# Чтение CSV файла с явным указанием разделителя
df = pd.read_csv(input_file)

# Проверка структуры DataFrame
print("Исходные данные:")
print(df.head())  # Покажем первые несколько строк для проверки

# Удаление ненужных столбцов
columns_to_drop = ['<TICKER>', '<PER>', '<TIME>', '<VOL>']
df.drop(columns=columns_to_drop, inplace=True)

# Преобразование столбца DATE в формат datetime
df['<DATE>'] = pd.to_datetime(df['<DATE>'], format='%d/%m/%y')

# Фильтрация строк по дате (оставляем только 18 и 20 ноября 2018 года)
filtered_df = df[(df['<DATE>'].dt.day == 18) | (df['<DATE>'] == '2018-11-20')]

# Сохранение отфильтрованного DataFrame в новый CSV файл
filtered_df.to_csv(output_file, index=False)

print(f"Файл '{output_file}' успешно сохранен.")
