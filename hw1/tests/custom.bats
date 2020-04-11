load harness

@test "custom-1" {
  check '7 + 3 * (10 / (12 / (3 + 1) - 1)) / (2 + 3) - 5 - 3 + (8)' '10'
}

@test "custom-2" {
  check '5 - - - + - (3 + 4) - +2' '10'
}

