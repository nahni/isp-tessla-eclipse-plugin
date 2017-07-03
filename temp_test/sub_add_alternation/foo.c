int add(int x, int y) {
  return x+y;
}

int sub(int x, int y) {
  return x-y;
}

int main() {
  int sum = 0;
  for (int i = 0; i < 5; i++) {
    sum = add(sum, sub(i,2));
  }
  sum = add(sum, add(21,21));
  for (int i = 0; i < 5; i++) {
    sum = add(sum, sub(i,2));
  }
}
