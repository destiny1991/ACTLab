#include <stdio.h>                                

int main() {                                      
    int n;                                        // 1
    int i;                                        // 2
    int sum;                                      // 3
    int tmp;                                      // 4
	
    scanf("%d", &n);                              // 5
    sum = 0;                                      // 6
    for(i = 1; i <= n; i++) {                     // 7
        tmp = i % 2;                              // 7.1
        if(tmp == 0) {                            // 7.2
            sum = sum + i;                        // 7.2.1
        } else {                                  // 7.3
            sum = sum - i * 2;                    // 7.3.1
            
        }                                         // 7.4
    }                                             // 8
    printf("sum is %d", sum);                     // 9

    return 0;                                     // 10
}                                                 
