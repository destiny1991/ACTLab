#include <stdio.h>

int main() {
    int n;
    int i;
    int sum;
	int tmp;
	
    scanf("%d", &n);
    tmp = n;
    for(i = 1; i < n; i++) {
        sum = sum + i * (i - i);
    }
    printf("sum is %d", sum);

    return 0;
}