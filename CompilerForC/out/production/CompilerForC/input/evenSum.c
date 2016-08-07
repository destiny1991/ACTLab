#include <stdio.h>

int main() {
    int n;
    int i;
    int sum;
    int tmp;
	
    scanf("%d", &n);
    sum = 0;
    for(i = 1; i <= n; i++) {
    	tmp = i % 2;
        if(tmp == 0) {
            sum = sum + i;
        } else {
        	sum = sum - i * 2;
        }
    }
    printf("sum is %d", sum);

    return 0;
}