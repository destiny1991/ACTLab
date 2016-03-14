#include <stdio.h>

int main() {
    int n;
    int i;
    int sum;
	int tmp;
	
    scanf("%d", &n);
    tmp = n;
    i = 1;
    i++;
    while(i < n) {
        sum = sum + i;
        i++;
    }
    printf("sum is %d", sum);

    return 0;
}