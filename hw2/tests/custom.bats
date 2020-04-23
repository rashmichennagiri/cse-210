load harness

@test "custom-1" {
  check '7 + 3 * (10 / (12 / (3 + 1) - 1)) / (2 + 3) - 5 - 3 + (8)' '10.0'
}

@test "custom-2" {
  check '5 - - - + - (3 + 4) - +2' '10'
}

@test "custom-3" {
  check '7 + (((3 * 2)))' '13'
}

@test "custom-4" {
  check '14 + 2 * 3 - 6 / 2' '17.0'
}

@test "custom-5" {
  check '3' '3'
}
