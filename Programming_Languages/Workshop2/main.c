#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int x;
    int y;
} Point;

typedef enum {
    Circle,
    Square,
    Triangle
} ShapeType;

typedef struct {
    Point p;
    char* name;
    ShapeType type;
} Shape;

typedef struct {
    Shape* shapes;
    int size;
} Container;

Container* create_container(){
    Container* ct = malloc(sizeof(Container));
    if (ct != NULL) {
        ct->size = 0;
        ct->shapes = NULL;
    }
    return ct;
}

int add_new_shape(Container* ct, char* name, Point p, ShapeType type) {
    if (ct == NULL) return -1;

    Shape* new_shapes = realloc(ct->shapes, (ct->size + 1) * sizeof(Shape));
    if (new_shapes == NULL) return -1;

    ct->shapes = new_shapes;
    ct->shapes[ct->size].p = p;
    ct->shapes[ct->size].type = type;
    // \0
    ct->shapes[ct->size].name = malloc(strlen(name) + 1);
    if (ct->shapes[ct->size].name == NULL) return -1;
    strcpy(ct->shapes[ct->size].name, name);

    ct->size++;
    return 0;
}

int remove_shape_by_index(Container* ct, int index) {
    if (ct == NULL || index < 0 || index >= ct->size) return -1;

    free(ct->shapes[index].name);

    for (int i = index; i < ct->size - 1; i++) {
        ct->shapes[i] = ct->shapes[i + 1];
    }

    Shape* new_shapes = realloc(ct->shapes, (ct->size - 1) * sizeof(Shape));
    if (new_shapes != NULL || ct->size - 1 == 0) {
        ct->shapes = new_shapes;
    }

    ct->size--;
    return 0;
}

void print(Container* ct) {
    if (ct == NULL) return;

    for (int i = 0; i < ct->size; i++) {
        printf("Shape %d: %s at (%d, %d), type: ",
               i,
               ct->shapes[i].name,
               ct->shapes[i].p.x,
               ct->shapes[i].p.y);

        switch (ct->shapes[i].type) {
            case Circle:   printf("Circle\n"); break;
            case Square:   printf("Square\n"); break;
            case Triangle: printf("Triangle\n"); break;
        }
    }
}

int main() {
    Container* container = create_container();
    if (container == NULL) return 1;

    Point p1 = {1, 2};
    add_new_shape(container, "FirstShape", p1, Circle);

    Point p2 = {3, 4};
    add_new_shape(container, "SecondShape", p2, Square);

    print(container);

    remove_shape_by_index(container, 0);
    printf("\nAfter removing index 0:\n");
    print(container);

    for (int i = 0; i < container->size; i++) {
        free(container->shapes[i].name);
    }
    free(container->shapes);
    free(container);

    return 0;
}
