1	#include <stdio.h>

3	int main() {
4	    int n;
5	    int i;
6	    int sum;
7	    int tmp;
	
9	    scanf("%d", &n);
10	    sum = 0;
11	    for(i = 1; i <= n; i++) {
12	        tmp = i % 2;
13	        if(tmp == 0) {
14	            sum = sum + i;
15	        } else {sum = sum - i * 2;
            
17	        }
18	    }
19	    printf("sum is %d", sum);

21	    return 0;
22	}
