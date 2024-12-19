import json
import csv
import os

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "in.json")
    output_file = os.path.join(os.path.dirname(__file__), "out-dop5.csv")

    string = open(input_file, "r").read()
    data = json.loads(string)
    with open(output_file, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file, delimiter=';', quoting=csv.QUOTE_ALL)

        # Записываем заголовки
        writer.writerow(['Day', 'Name', 'Time', 'Teacher', 'Class', 'Building'])

        # Проходим по каждому дню и его расписанию
        for day_info in data['Schedule']:
            day = day_info['Day']
            for schedule_item in day_info['Schedule']:
                writer.writerow([day, schedule_item['Name'], schedule_item['Time'], schedule_item['Teacher'],
                                 schedule_item['Class'], schedule_item['Building']])

    print(f'Data has been written to {output_file}')