Ονοματεπώνυμο: Θεοφανόπουλος Μιχαήλ
Αριθμός Μητρώου: 1115201800053
3η εργασία Μεταγλωττιστές Εαρινό 2021

-> Όλα τα αρχεία τα έχω πάρει αυτούσια από τη δεύτερη εργασία μου. Η μόνη διαφορά είναι πως έχει προστεθεί ένας επιπλέον visitor, ο LLVMvisitor, στο αρχείο LLVMvisitor.java. Ο visitor αυτός καλείται όπως και οι προηγούμενοι του στο Main.java. Το κομμάτι που είχα υλοποιήσει και εκτύπωνα τα offsets καλώντας την printOffsets() στη προηγούμενη εργασία μου, για τη δεδομένη υλοποίηση έχει παραλειφθεί.

-> Η εντολή μεταγλώττισης του προγράμματος είναι > make compile.

-> Για να εκτελεστεί το πρόγραμμα έχω συμπεριλάβει την εντολή > make run, η οποία τρέχει το πρόγραμμα χρησιμοποιώντας το αρχείο Example.java στο οποίο by default έχω αφήσει ένα από τα παραδείγματα llvm που δώθηκαν με την εκφώνηση.

-> Το Output του προγράμματος γράφεται σε ένα (ή περισσότερα) αρχείο/α x.ll, όπου x το/τα .java πρόγραμμα/τα που δώθηκε/αν σαν παράμετροι.

-> Για να υλοποιήσω το ζητούμενο της άσκησης χρησιμοποίησα δύο vtables, ένα για κάθε κλάση και τις συναρτήσεις της και ένα για κάθε field μιας κλάσης.

-> Εκτός από τα vtables χρησιμοποιώ και 2 LinkedHashMaps με σκοπό την διευκόλυνση του προγράμματος όσον αφορά την αναδρομική κλήση visitors που επιστρέφουν register, όπως και σε άλλες λειτουργίες.

-> Για τους registers και τα labels που χρησιμοποιώ σε αρκετά σημεία του LLVM IR πρόγράμματος που παράγω, κρατώ δύο counters οι οποίοι αναθέτουν κάθε φορά έναν καινούργιο register/label για χρήστη από το πρόγραμμα. 

Σας εύχομαι καλή διόρθωση!