#include <stdio.h>

int main() {
	int i;
	int j;
	int k;
	int p;
	int z;
	
	scanf("%d %d", &i, &j);
	if(i < j) {
		printf("%d < %d", i, j);
	} else {
		printf("%d >= %d", i, j);
	}

	return 0;
}