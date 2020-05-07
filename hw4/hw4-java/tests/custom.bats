load harness

@test "custom-1" {
  check 'x := [1,2,3]' '{x → [1,2,3]}'
}

@test "custom-2" {
  check 'x := [1,2] ; y := [3,4]' '{x → [1,2], y → [3,4]}'
}

@test "custom-3" {
  check 'while 0 < z * -4 do z := -1' '{}'
}

@test "custom-4" {
  check 'if x = 3 ∨ y < -1 then x := 1 else x := 9' '{x → 9}'
}

@test "custom-5" {
  check 'i := 10 ; sum := 1 ; while 0 < i do { sum := sum + i ; i := i - 1 }' '{i → 0, sum → 56}'
}
