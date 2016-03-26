#include <stdio.h>

int main() {
    int n;
    int i;
    int sum;
	int tmp;
	
    scanf("%d", &n);
    i = 1;
    while(i < n) {
    	sum = sum + i;
        if(sum == 10) {
        	printf("sum == 10");
        } else {
        	printf("else else");
        }
        i++;
    }
    
    return 0;
}
