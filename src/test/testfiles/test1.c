

int main(int argc, char** argv) {
    int a = argc;
    if (a == 4)
        a += 2;
    else
        a += 3;

    int i = 0;
    for (i = 0; i < 10; ++i) {
        a = i + 1;
    }

    while (i < 20)
        a += i;

    do {
        a += i;
    } while (i < 30);

    return 0;
}