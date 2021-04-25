1.expr = term expr2

3.expr2 = + term expr2
4.	  |- term expr2
5.	  | ε
	  
7.term = number term2

9.term2 = ** number term2
10.	  | ε
	  
12.number = digit mul
13.	  | (expr)
	   
15.mul = number
16.	  | ε
	   
18.digit = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 

FIRST(1) = FIRST(term) = FIRST(number) = {0,1,2,3,4,5,6,7,8,9,(}
FIRST(3) = {+}
FIRST(4) = {-}
FIRST(5) = {ε}
FIRST(7) = FIRST(number) = {0,1,2,3,4,5,6,7,8,9,(}
FIRST(9) = {**}
FIRST(10) = {ε}
FIRST(12) = FIRST(digit) = {0,1,2,3,4,5,6,7,8,9}
FIRST(13) = {(}
FIRST(15) = FIRST(number) = {0,1,2,3,4,5,6,7,8,9,(}
FIRST(16) = {ε}

FOLLOW(expr) = {)}
FOLLOW(expr2) = {$}
FOLLOW(term) = FIRST(expr2) = {+,-,$}
FOLLOW(term2) = {$}
FOLLOW(number) = {**,$}
FOLLOW(mul) = {$}
FOLLOW(digit) = {(,0,1,2,3,4,5,6,7,8,9,$}

FIRST+(1) = FIRST(term) = FIRST(number) = FIRST(digit) = {0,1,2,3,4,5,6,7,8,9,(}

FIRST+(3) = {+}
FIRST+(4) = {-}
FIRST+(5) = {ε}

FIRST+(7) = FIRST(number) = {0,1,2,3,4,5,6,7,8,9,(}

FIRST+(9) = {**}
FIRST+(10) = {ε}

FIRST+(12) = FIRST(digit) = {0,1,2,3,4,5,6,7,8,9}
FIRST+(13) = {(}

FIRST+(15) = {0,1,2,3,4,5,6,7,8,9,(}
FIRST+(16) = {ε}

FIRST+(18) = {0,1,2,3,4,5,6,7,8,9}

-> Η γραμματική είναι σε μορφή LL(1) καθώς δεν υπάρχει left recursion και τα first+ των γραμμών για κάθε κανόνα έχουν ως τομή το κενό σύνολο.

-> Το πρόγραμμα μου ακολουθεί τη δομή του προγράμματος που δόθηκε στο πρώτο εργαστήριο του μαθήματος, αλλά υλοποιεί τη γραμματική που αναφέρεται στην εκφώνηση της εργασίας. Για να το πετύχει αυτό, χρησιμοποιεί τη μέθοδο που είδαμε στο μάθημα για τροποποίηση μιας γραμματικής με σκοπό τη μετατροπή της σε τύπου LL(1).

-> Έχω αλλάξει το ParseError throwing που είχε το αρχικό πρόγραμμα σε ακρετά σημεία, παρόλα αυτά το πρόγραμμα εκτυπώνει τα σωστά parse errors για όλα τα ενδεχόμενα. 