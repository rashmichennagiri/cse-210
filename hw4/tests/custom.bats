load harness

@test "custom-1" {
  check 'x := [1,2,3]' '⇒ skip, {x → [1, 2, 3]}'
}

@test "custom-2" {
  check 'x := [1,2] ; y := [3,4]' '⇒ skip; y := [3, 4], {x → [1, 2]}
⇒ y := [3, 4], {x → [1, 2]}
⇒ skip, {x → [1, 2], y → [3, 4]}'
}

@test "custom-3" {
  check 'while 0 < z * -4 do z := -1' '⇒ skip, {}'
}

@test "custom-4" {
  check 'if x = 3 ∨ y < -1 then x := 1 else x := 9' '⇒ x := 9, {}
⇒ skip, {x → 9}'
}

@test "custom-5" {
  check 'i := 10 ; sum := 1 ; while 0 < i do { sum := sum + i ; i := i - 1 }' '⇒ skip; sum := 1; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10}
⇒ sum := 1; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10, sum → 1}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10, sum → 1}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10, sum → 1}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10, sum → 11}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 10, sum → 11}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 9, sum → 11}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 9, sum → 11}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 9, sum → 11}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 9, sum → 20}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 9, sum → 20}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 8, sum → 20}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 8, sum → 20}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 8, sum → 20}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 8, sum → 28}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 8, sum → 28}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 7, sum → 28}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 7, sum → 28}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 7, sum → 28}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 7, sum → 35}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 7, sum → 35}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 6, sum → 35}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 6, sum → 35}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 6, sum → 35}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 6, sum → 41}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 6, sum → 41}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 5, sum → 41}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 5, sum → 41}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 5, sum → 41}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 5, sum → 46}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 5, sum → 46}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 4, sum → 46}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 4, sum → 46}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 4, sum → 46}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 4, sum → 50}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 4, sum → 50}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 3, sum → 50}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 3, sum → 50}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 3, sum → 50}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 3, sum → 53}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 3, sum → 53}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 2, sum → 53}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 2, sum → 53}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 2, sum → 53}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 2, sum → 55}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 2, sum → 55}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 1, sum → 55}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 1, sum → 55}
⇒ sum := (sum+i); i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 1, sum → 55}
⇒ skip; i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 1, sum → 56}
⇒ i := (i-1); while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 1, sum → 56}
⇒ skip; while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 0, sum → 56}
⇒ while (0<i) do { sum := (sum+i); i := (i-1) }, {i → 0, sum → 56}
⇒ skip, {i → 0, sum → 56}'
}
