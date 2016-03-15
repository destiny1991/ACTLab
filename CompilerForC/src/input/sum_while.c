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
    
    if(sum > n) {
    	printf("sum %d is bigger than n %d", n, sum);
    } else {
    	printf("less!");
    }

    return 0;
}
