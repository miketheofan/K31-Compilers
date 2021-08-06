@.Main_vtable = global [0 x i8*] []
@.A_vtable = global [2 x i8*] [i8* bitcast (i32 (i8*)* @A.foo to i8*),i8* bitcast (i32 (i8*,i32,i1)* @A.bar to i8*)]


declare i8* @calloc(i32, i32)
declare i32 @printf(i8*, ...)
declare void @exit(i32)

@_cint = constant [4 x i8] c"%d\0a\00"
@_cOOB = constant [15 x i8] c"Out of bounds\0a\00"
define void @print_int(i32 %i) {
	%_str = bitcast [4 x i8]* @_cint to i8*
	call i32 (i8*, ...) @printf(i8* %_str, i32 %i)
	ret void
}

define void @throw_oob() {
	%_str = bitcast [15 x i8]* @_cOOB to i8*
	call i32 (i8*, ...) @printf(i8* %_str)
	call void @exit(i32 1)
	ret void
}

define i32 @main() {
	%dummy = alloca i32
	%a = alloca i8*
	%_0 = call i8* @calloc(i32 1, i32 8)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [2 x i8*], [2 x i8*]* @.A_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %a
	%_3 = load i8*, i8** %a
	%_4 = bitcast i8* %_3 to i8***

	; A.foo : 0
	%_5 = load i8**, i8*** %_4
	%_6 = getelementptr i8*, i8** %_5, i32 0
	%_7 = load i8*, i8** %_6
	%_8 = bitcast i8* %_7 to i32 (i8*)*
	%_9 = call i32 %_8(i8* %_3)
	store i32 %_9, i32* %dummy
	ret i32 0

}

define i32 @A.foo(i8* %this) {
	%a = alloca i32
	%b = alloca i32
	store i32 3, i32* %a
	br label %loop0

loop0:
	%_10 = load i32, i32* %a
	%_11 = icmp slt i32 %_10, 4
	br i1 %_11, label %loop1, label %loop2

loop1:
	%_12 = load i32, i32* %a
	%_13 = add i32 %_12, 1
	store i32 %_13, i32* %a


	br label %loop0

loop2:
	%_14 = load i32, i32* %a

	call void (i32) @print_int(i32 %_14)
	%_15 = bitcast i8* %this to i8***

	; A.bar : 1
	%_16 = load i8**, i8*** %_15
	%_17 = getelementptr i8*, i8** %_16, i32 1
	%_18 = load i8*, i8** %_17
	%_19 = bitcast i8* %_18 to i32 (i8*,i32,i1)*
	%_20 = call i32 %_19(i8* %this,i32 7,i1 1)
	store i32 %_20, i32* %b
	%_23 = load i32, i32* %b

	call void (i32) @print_int(i32 %_23)
	ret i32 0

}

define i32 @A.bar(i8* %this, i32 %.a, i1 %.cond) {
	%a = alloca i32
	store i32 %.a, i32* %a
	%cond = alloca i1
	store i1 %.cond, i1* %cond
	%b = alloca i32
	br label %loop3

loop3:
	%_24 = load i1, i1* %cond
	br i1 %_24, label %loop4, label %loop5

loop4:
	%_25 = load i32, i32* %a
	store i32 %_25, i32* %b
	%_26 = load i1, i1* %cond
	br i1 %_26, label %if6, label %if7

if6:
	store i32 2, i32* %a
	br label %if8

if7:
	br label %if8

if8:
	store i1 0, i1* %cond


	br label %loop3

loop5:
	%_27 = load i32, i32* %b
	ret i32 %_27

}