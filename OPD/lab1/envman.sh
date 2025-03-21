#!/usr/bin/bash

CONFIG_FILE="$HOME/.envman_vars"

show_help() {
    echo "Usage: $0 [command] [options]"
    echo "Commands:"
    echo "  add [NAME] [VALUE]    Add a new environment variable"
    echo "  remove [NAME]         Remove an environment variable"
    echo "  list                  List all managed environment variables"
}

# Создать переменную
add_variable() {
    local name=$1
    local value=$2

    # Проверяем, указаны ли имя и значение
    if [[ -z $name || -z $value ]]; then
        echo "Error: Name and value are required."
        exit 1
    fi

    if grep -q "export $name=" "$CONFIG_FILE"; then
        sed -i.bak "/export $name=/d" "$CONFIG_FILE"
    fi

    echo "export $name=\"$value\"" >> "$CONFIG_FILE"
    echo "Variable '$name' added."
	source "$CONFIG_FILE"

}

remove_variable() {
    local name=$1
    local silent=$2


    if [[ -z $name ]]; then
        echo "Error: Name is required."
        exit 1
    fi

    if grep -q "export $name=" "$CONFIG_FILE"; then
        sed -i '' "/export $name=/d" "$CONFIG_FILE"
        [[ $silent != "silent" ]] && echo "Variable '$name' removed."
    else
        [[ $silent != "silent" ]] && echo "Error: Variable '$name' does not exist."
    fi
	source "$CONFIG_FILE"
}

list_variables() {
    if [ ! -s "$CONFIG_FILE" ]; then
        echo "No managed environment variables found."
    else
        echo "Managed environment variables:"
        cat "$CONFIG_FILE"
    fi
}

case $1 in
    add)
        add_variable "$2" "$3"
        ;;
    remove)
        remove_variable "$2"
        ;;
    list)
        list_variables
        ;;
    *)
        show_help
        ;;
esac
