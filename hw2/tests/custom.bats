load harness

@test "custom-1" {
  check 'while 0 = z * -4 do z := -1' '{z → -1}'
}

@test "custom-2" {
  check 'while 0 = z * -4 do z := -1' '{z → -1}'
}

@test "custom-3" {
  check 'while 0 = z * -4 do z := -1' '{z → -1}'
}

@test "custom-4" {
  check 'if x = 0 ∧ y < 4 then x := 1 else x := 3' '{x → 1}'
}

@test "custom-5" {
  check 'i := 5 ; fact := 1 ; while 0 < i do { fact := fact * i ; i := i - 1 }' '{fact → 120, i → 0}'
}
