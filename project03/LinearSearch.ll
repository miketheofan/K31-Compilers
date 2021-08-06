@.TreeVisitor_vtable = global [0 x i8*] []
@.TV_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @TV.Start to i8*)]@.Tree_vtable = global [21 x i8*] [i8* bitcast (i1 (i8*,i32)* @Tree.Init to i8*),i8* bitcast (i1 (i8*,i8*)* @Tree.SetRight to i8*),i8* bitcast (i1 (i8*,i8*)* @Tree.SetLeft to i8*),i8* bitcast (i8* (i8*)* @Tree.GetRight to i8*),i8* bitcast (i8* (i8*)* @Tree.GetLeft to i8*),i8* bitcast (i32 (i8*)* @Tree.GetKey to i8*),i8* bitcast (i1 (i8*,i32)* @Tree.SetKey to i8*),i8* bitcast (i1 (i8*)* @Tree.GetHas_Right to i8*),i8* bitcast (i1 (i8*)* @Tree.GetHas_Left to i8*),i8* bitcast (i1 (i8*,i1)* @Tree.SetHas_Left to i8*),i8* bitcast (i1 (i8*,i1)* @Tree.SetHas_Right to i8*),i8* bitcast (i1 (i8*,i32,i32)* @Tree.Compare to i8*),i8* bitcast (i1 (i8*,i32)* @Tree.Insert to i8*),i8* bitcast (i1 (i8*,i32)* @Tree.Delete to i8*),i8* bitcast (i1 (i8*,i8*,i8*)* @Tree.Remove to i8*),i8* bitcast (i1 (i8*,i8*,i8*)* @Tree.RemoveRight to i8*),i8* bitcast (i1 (i8*,i8*,i8*)* @Tree.RemoveLeft to i8*),i8* bitcast (i32 (i8*,i32)* @Tree.Search to i8*),i8* bitcast (i1 (i8*)* @Tree.Print to i8*),i8* bitcast (i1 (i8*,i8*)* @Tree.RecPrint to i8*),i8* bitcast (i32 (i8*,i8*)* @Tree.accept to i8*)]@.Visitor_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*,i8*)* @Visitor.visit to i8*)]@.MyVisitor_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*,i8*)* @MyVisitor.visit to i8*)]


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
	%_0 = call i8* @calloc(i32 1, i32 8)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.TV_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = bitcast i8* %_0 to i8***

	; TV.Start : 0
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*)*
	%_8 = call i32 %_7(i8* %_0)

	call void (i32) @print_int(i32 %_8)
	ret i32 0

}

define i32 @TV.Start(i8* %this) {
	%root = alloca i8*
	%ntb = alloca i1
	%nti = alloca i32
	%v = alloca i8*
	%_9 = call i8* @calloc(i32 1, i32 38)
	%_10 = bitcast i8* %_9 to i8***
	%_11 = getelementptr [21 x i8*], [21 x i8*]* @.Tree_vtable, i32 0, i32 0
	store i8** %_11, i8*** %_10
	store i8* %_9, i8** %root
	%_12 = load i8*, i8** %root
	%_13 = bitcast i8* %_12 to i8***

	; Tree.Init : 0
	%_14 = load i8**, i8*** %_13
	%_15 = getelementptr i8*, i8** %_14, i32 0
	%_16 = load i8*, i8** %_15
	%_17 = bitcast i8* %_16 to i1 (i8*,i32)*
	%_18 = call i1 %_17(i8* %_12,i32 16)
	store i1 %_18, i1* %ntb
	%_20 = load i8*, i8** %root
	%_21 = bitcast i8* %_20 to i8***

	; Tree.Print : 18
	%_22 = load i8**, i8*** %_21
	%_23 = getelementptr i8*, i8** %_22, i32 18
	%_24 = load i8*, i8** %_23
	%_25 = bitcast i8* %_24 to i1 (i8*)*
	%_26 = call i1 %_25(i8* %_20)
	store i1 %_26, i1* %ntb

	call void (i32) @print_int(i32 100000000)
	%_27 = load i8*, i8** %root
	%_28 = bitcast i8* %_27 to i8***

	; Tree.Insert : 12
	%_29 = load i8**, i8*** %_28
	%_30 = getelementptr i8*, i8** %_29, i32 12
	%_31 = load i8*, i8** %_30
	%_32 = bitcast i8* %_31 to i1 (i8*,i32)*
	%_33 = call i1 %_32(i8* %_27,i32 8)
	store i1 %_33, i1* %ntb
	%_35 = load i8*, i8** %root
	%_36 = bitcast i8* %_35 to i8***

	; Tree.Insert : 12
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 12
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i1 (i8*,i32)*
	%_41 = call i1 %_40(i8* %_35,i32 24)
	store i1 %_41, i1* %ntb
	%_43 = load i8*, i8** %root
	%_44 = bitcast i8* %_43 to i8***

	; Tree.Insert : 12
	%_45 = load i8**, i8*** %_44
	%_46 = getelementptr i8*, i8** %_45, i32 12
	%_47 = load i8*, i8** %_46
	%_48 = bitcast i8* %_47 to i1 (i8*,i32)*
	%_49 = call i1 %_48(i8* %_43,i32 4)
	store i1 %_49, i1* %ntb
	%_51 = load i8*, i8** %root
	%_52 = bitcast i8* %_51 to i8***

	; Tree.Insert : 12
	%_53 = load i8**, i8*** %_52
	%_54 = getelementptr i8*, i8** %_53, i32 12
	%_55 = load i8*, i8** %_54
	%_56 = bitcast i8* %_55 to i1 (i8*,i32)*
	%_57 = call i1 %_56(i8* %_51,i32 12)
	store i1 %_57, i1* %ntb
	%_59 = load i8*, i8** %root
	%_60 = bitcast i8* %_59 to i8***

	; Tree.Insert : 12
	%_61 = load i8**, i8*** %_60
	%_62 = getelementptr i8*, i8** %_61, i32 12
	%_63 = load i8*, i8** %_62
	%_64 = bitcast i8* %_63 to i1 (i8*,i32)*
	%_65 = call i1 %_64(i8* %_59,i32 20)
	store i1 %_65, i1* %ntb
	%_67 = load i8*, i8** %root
	%_68 = bitcast i8* %_67 to i8***

	; Tree.Insert : 12
	%_69 = load i8**, i8*** %_68
	%_70 = getelementptr i8*, i8** %_69, i32 12
	%_71 = load i8*, i8** %_70
	%_72 = bitcast i8* %_71 to i1 (i8*,i32)*
	%_73 = call i1 %_72(i8* %_67,i32 28)
	store i1 %_73, i1* %ntb
	%_75 = load i8*, i8** %root
	%_76 = bitcast i8* %_75 to i8***

	; Tree.Insert : 12
	%_77 = load i8**, i8*** %_76
	%_78 = getelementptr i8*, i8** %_77, i32 12
	%_79 = load i8*, i8** %_78
	%_80 = bitcast i8* %_79 to i1 (i8*,i32)*
	%_81 = call i1 %_80(i8* %_75,i32 14)
	store i1 %_81, i1* %ntb
	%_83 = load i8*, i8** %root
	%_84 = bitcast i8* %_83 to i8***

	; Tree.Print : 18
	%_85 = load i8**, i8*** %_84
	%_86 = getelementptr i8*, i8** %_85, i32 18
	%_87 = load i8*, i8** %_86
	%_88 = bitcast i8* %_87 to i1 (i8*)*
	%_89 = call i1 %_88(i8* %_83)
	store i1 %_89, i1* %ntb

	call void (i32) @print_int(i32 100000000)
	%_90 = call i8* @calloc(i32 1, i32 8)
	%_91 = bitcast i8* %_90 to i8***
	%_92 = getelementptr [1 x i8*], [1 x i8*]* @.MyVisitor_vtable, i32 0, i32 0
	store i8** %_92, i8*** %_91
	store i8* %_90, i8** %v

	call void (i32) @print_int(i32 50000000)
	%_93 = load i8*, i8** %root
	%_94 = bitcast i8* %_93 to i8***

	; Tree.accept : 20
	%_95 = load i8**, i8*** %_94
	%_96 = getelementptr i8*, i8** %_95, i32 20
	%_97 = load i8*, i8** %_96
	%_98 = bitcast i8* %_97 to i32 (i8*,i8*)*
	%_99 = load i8*, i8** %v
	%_100 = call i32 %_98(i8* %_93,i8* %_99)
	store i32 %_100, i32* %nti

	call void (i32) @print_int(i32 100000000)
	%_102 = load i8*, i8** %root
	%_103 = bitcast i8* %_102 to i8***

	; Tree.Search : 17
	%_104 = load i8**, i8*** %_103
	%_105 = getelementptr i8*, i8** %_104, i32 17
	%_106 = load i8*, i8** %_105
	%_107 = bitcast i8* %_106 to i32 (i8*,i32)*
	%_108 = call i32 %_107(i8* %_102,i32 24)

	call void (i32) @print_int(i32 %_108)
	%_110 = load i8*, i8** %root
	%_111 = bitcast i8* %_110 to i8***

	; Tree.Search : 17
	%_112 = load i8**, i8*** %_111
	%_113 = getelementptr i8*, i8** %_112, i32 17
	%_114 = load i8*, i8** %_113
	%_115 = bitcast i8* %_114 to i32 (i8*,i32)*
	%_116 = call i32 %_115(i8* %_110,i32 12)

	call void (i32) @print_int(i32 %_116)
	%_118 = load i8*, i8** %root
	%_119 = bitcast i8* %_118 to i8***

	; Tree.Search : 17
	%_120 = load i8**, i8*** %_119
	%_121 = getelementptr i8*, i8** %_120, i32 17
	%_122 = load i8*, i8** %_121
	%_123 = bitcast i8* %_122 to i32 (i8*,i32)*
	%_124 = call i32 %_123(i8* %_118,i32 16)

	call void (i32) @print_int(i32 %_124)
	%_126 = load i8*, i8** %root
	%_127 = bitcast i8* %_126 to i8***

	; Tree.Search : 17
	%_128 = load i8**, i8*** %_127
	%_129 = getelementptr i8*, i8** %_128, i32 17
	%_130 = load i8*, i8** %_129
	%_131 = bitcast i8* %_130 to i32 (i8*,i32)*
	%_132 = call i32 %_131(i8* %_126,i32 50)

	call void (i32) @print_int(i32 %_132)
	%_134 = load i8*, i8** %root
	%_135 = bitcast i8* %_134 to i8***

	; Tree.Search : 17
	%_136 = load i8**, i8*** %_135
	%_137 = getelementptr i8*, i8** %_136, i32 17
	%_138 = load i8*, i8** %_137
	%_139 = bitcast i8* %_138 to i32 (i8*,i32)*
	%_140 = call i32 %_139(i8* %_134,i32 12)

	call void (i32) @print_int(i32 %_140)
	%_142 = load i8*, i8** %root
	%_143 = bitcast i8* %_142 to i8***

	; Tree.Delete : 13
	%_144 = load i8**, i8*** %_143
	%_145 = getelementptr i8*, i8** %_144, i32 13
	%_146 = load i8*, i8** %_145
	%_147 = bitcast i8* %_146 to i1 (i8*,i32)*
	%_148 = call i1 %_147(i8* %_142,i32 12)
	store i1 %_148, i1* %ntb
	%_150 = load i8*, i8** %root
	%_151 = bitcast i8* %_150 to i8***

	; Tree.Print : 18
	%_152 = load i8**, i8*** %_151
	%_153 = getelementptr i8*, i8** %_152, i32 18
	%_154 = load i8*, i8** %_153
	%_155 = bitcast i8* %_154 to i1 (i8*)*
	%_156 = call i1 %_155(i8* %_150)
	store i1 %_156, i1* %ntb
	%_157 = load i8*, i8** %root
	%_158 = bitcast i8* %_157 to i8***

	; Tree.Search : 17
	%_159 = load i8**, i8*** %_158
	%_160 = getelementptr i8*, i8** %_159, i32 17
	%_161 = load i8*, i8** %_160
	%_162 = bitcast i8* %_161 to i32 (i8*,i32)*
	%_163 = call i32 %_162(i8* %_157,i32 12)

	call void (i32) @print_int(i32 %_163)
	ret i32 0

}

define i1 @Tree.Init(i8* %this, i32 %.v_key) {
	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key
	%_165 = load i32, i32* %v_key
	%_166 = getelementptr i8, i8* %this , i32 24
	%_167 = bitcast i8* %_166 to i32*
	store i32 %_165, i32* %_167
	%_168 = getelementptr i8, i8* %this , i32 28
	%_169 = bitcast i8* %_168 to i1*
	store i1 0, i1* %_169
	%_170 = getelementptr i8, i8* %this , i32 29
	%_171 = bitcast i8* %_170 to i1*
	store i1 0, i1* %_171
	ret i1 1

}

define i1 @Tree.SetRight(i8* %this, i8* %.rn) {
	%rn = alloca i8*
	store i8* %.rn, i8** %rn
	%_172 = load i8*, i8** %rn
	%_173 = getelementptr i8, i8* %this , i32 16
	%_174 = bitcast i8* %_173 to i8**
	store i8* %_172, i8** %_174
	ret i1 1

}

define i1 @Tree.SetLeft(i8* %this, i8* %.ln) {
	%ln = alloca i8*
	store i8* %.ln, i8** %ln
	%_175 = load i8*, i8** %ln
	%_176 = getelementptr i8, i8* %this , i32 8
	%_177 = bitcast i8* %_176 to i8**
	store i8* %_175, i8** %_177
	ret i1 1

}

define i8* @Tree.GetRight(i8* %this) {
	%_178 = getelementptr i8, i8* %this , i32 16
	%_179 = bitcast i8* %_178 to i8**
	%_180 = load i8*, i8** %_179
	ret i8* %_180

}

define i8* @Tree.GetLeft(i8* %this) {
	%_181 = getelementptr i8, i8* %this , i32 8
	%_182 = bitcast i8* %_181 to i8**
	%_183 = load i8*, i8** %_182
	ret i8* %_183

}

define i32 @Tree.GetKey(i8* %this) {
	%_184 = getelementptr i8, i8* %this , i32 24
	%_185 = bitcast i8* %_184 to i32*
	%_186 = load i32, i32* %_185
	ret i32 %_186

}

define i1 @Tree.SetKey(i8* %this, i32 %.v_key) {
	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key
	%_187 = load i32, i32* %v_key
	%_188 = getelementptr i8, i8* %this , i32 24
	%_189 = bitcast i8* %_188 to i32*
	store i32 %_187, i32* %_189
	ret i1 1

}

define i1 @Tree.GetHas_Right(i8* %this) {
	%_190 = getelementptr i8, i8* %this , i32 29
	%_191 = bitcast i8* %_190 to i1*
	%_192 = load i1, i1* %_191
	ret i1 %_192

}

define i1 @Tree.GetHas_Left(i8* %this) {
	%_193 = getelementptr i8, i8* %this , i32 28
	%_194 = bitcast i8* %_193 to i1*
	%_195 = load i1, i1* %_194
	ret i1 %_195

}

define i1 @Tree.SetHas_Left(i8* %this, i1 %.val) {
	%val = alloca i1
	store i1 %.val, i1* %val
	%_196 = load i1, i1* %val
	%_197 = getelementptr i8, i8* %this , i32 28
	%_198 = bitcast i8* %_197 to i1*
	store i1 %_196, i1* %_198
	ret i1 1

}

define i1 @Tree.SetHas_Right(i8* %this, i1 %.val) {
	%val = alloca i1
	store i1 %.val, i1* %val
	%_199 = load i1, i1* %val
	%_200 = getelementptr i8, i8* %this , i32 29
	%_201 = bitcast i8* %_200 to i1*
	store i1 %_199, i1* %_201
	ret i1 1

}

define i1 @Tree.Compare(i8* %this, i32 %.num1, i32 %.num2) {
	%num1 = alloca i32
	store i32 %.num1, i32* %num1
	%num2 = alloca i32
	store i32 %.num2, i32* %num2
	%ntb = alloca i1
	%nti = alloca i32
	store i1 0, i1* %ntb
	%_202 = load i32, i32* %num2
	%_203 = add i32 %_202, 1
	store i32 %_203, i32* %nti
	%_204 = load i32, i32* %num1
	%_205 = load i32, i32* %num2
	%_206 = icmp slt i32 %_204, %_205
	br i1 %_206, label %if0, label %if1

if0:
	store i1 0, i1* %ntb
	br label %if2

if1:
	%_207 = load i32, i32* %num1
	%_208 = load i32, i32* %nti
	%_209 = icmp slt i32 %_207, %_208
	%_210 = xor i1 1, %_209
	br i1 %_210, label %if3, label %if4

if3:
	store i1 0, i1* %ntb
	br label %if5

if4:
	store i1 1, i1* %ntb
	br label %if5

if5:
	br label %if2

if2:
	%_211 = load i1, i1* %ntb
	ret i1 %_211

}

define i1 @Tree.Insert(i8* %this, i32 %.v_key) {
	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key
	%new_node = alloca i8*
	%ntb = alloca i1
	%current_node = alloca i8*
	%cont = alloca i1
	%key_aux = alloca i32
	%_212 = call i8* @calloc(i32 1, i32 38)
	%_213 = bitcast i8* %_212 to i8***
	%_214 = getelementptr [21 x i8*], [21 x i8*]* @.Tree_vtable, i32 0, i32 0
	store i8** %_214, i8*** %_213
	store i8* %_212, i8** %new_node
	%_215 = load i8*, i8** %new_node
	%_216 = bitcast i8* %_215 to i8***

	; Tree.Init : 0
	%_217 = load i8**, i8*** %_216
	%_218 = getelementptr i8*, i8** %_217, i32 0
	%_219 = load i8*, i8** %_218
	%_220 = bitcast i8* %_219 to i1 (i8*,i32)*
	%_221 = load i32, i32* %v_key
	%_222 = call i1 %_220(i8* %_215,i32 %_221)
	store i1 %_222, i1* %ntb
	store i8* %this, i8** %current_node
	store i1 1, i1* %cont
	br label %loop6

loop6:
	%_224 = load i1, i1* %cont
	br i1 %_224, label %loop7, label %loop8

loop7:
	%_225 = load i8*, i8** %current_node
	%_226 = bitcast i8* %_225 to i8***

	; Tree.GetKey : 5
	%_227 = load i8**, i8*** %_226
	%_228 = getelementptr i8*, i8** %_227, i32 5
	%_229 = load i8*, i8** %_228
	%_230 = bitcast i8* %_229 to i32 (i8*)*
	%_231 = call i32 %_230(i8* %_225)
	store i32 %_231, i32* %key_aux
	%_232 = load i32, i32* %v_key
	%_233 = load i32, i32* %key_aux
	%_234 = icmp slt i32 %_232, %_233
	br i1 %_234, label %if9, label %if10

if9:
	%_235 = load i8*, i8** %current_node
	%_236 = bitcast i8* %_235 to i8***

	; Tree.GetHas_Left : 8
	%_237 = load i8**, i8*** %_236
	%_238 = getelementptr i8*, i8** %_237, i32 8
	%_239 = load i8*, i8** %_238
	%_240 = bitcast i8* %_239 to i1 (i8*)*
	%_241 = call i1 %_240(i8* %_235)
	br i1 %_241, label %if12, label %if13

if12:
	%_242 = load i8*, i8** %current_node
	%_243 = bitcast i8* %_242 to i8***

	; Tree.GetLeft : 4
	%_244 = load i8**, i8*** %_243
	%_245 = getelementptr i8*, i8** %_244, i32 4
	%_246 = load i8*, i8** %_245
	%_247 = bitcast i8* %_246 to i8* (i8*)*
	%_248 = call i8* %_247(i8* %_242)
	store i8* %_248, i8** %current_node
	br label %if14

if13:
	store i1 0, i1* %cont
	%_249 = load i8*, i8** %current_node
	%_250 = bitcast i8* %_249 to i8***

	; Tree.SetHas_Left : 9
	%_251 = load i8**, i8*** %_250
	%_252 = getelementptr i8*, i8** %_251, i32 9
	%_253 = load i8*, i8** %_252
	%_254 = bitcast i8* %_253 to i1 (i8*,i1)*
	%_255 = call i1 %_254(i8* %_249,i1 1)
	store i1 %_255, i1* %ntb
	%_257 = load i8*, i8** %current_node
	%_258 = bitcast i8* %_257 to i8***

	; Tree.SetLeft : 2
	%_259 = load i8**, i8*** %_258
	%_260 = getelementptr i8*, i8** %_259, i32 2
	%_261 = load i8*, i8** %_260
	%_262 = bitcast i8* %_261 to i1 (i8*,i8*)*
	%_263 = load i8*, i8** %new_node
	%_264 = call i1 %_262(i8* %_257,i8* %_263)
	store i1 %_264, i1* %ntb
	br label %if14

if14:
	br label %if11

if10:
	%_266 = load i8*, i8** %current_node
	%_267 = bitcast i8* %_266 to i8***

	; Tree.GetHas_Right : 7
	%_268 = load i8**, i8*** %_267
	%_269 = getelementptr i8*, i8** %_268, i32 7
	%_270 = load i8*, i8** %_269
	%_271 = bitcast i8* %_270 to i1 (i8*)*
	%_272 = call i1 %_271(i8* %_266)
	br i1 %_272, label %if15, label %if16

if15:
	%_273 = load i8*, i8** %current_node
	%_274 = bitcast i8* %_273 to i8***

	; Tree.GetRight : 3
	%_275 = load i8**, i8*** %_274
	%_276 = getelementptr i8*, i8** %_275, i32 3
	%_277 = load i8*, i8** %_276
	%_278 = bitcast i8* %_277 to i8* (i8*)*
	%_279 = call i8* %_278(i8* %_273)
	store i8* %_279, i8** %current_node
	br label %if17

if16:
	store i1 0, i1* %cont
	%_280 = load i8*, i8** %current_node
	%_281 = bitcast i8* %_280 to i8***

	; Tree.SetHas_Right : 10
	%_282 = load i8**, i8*** %_281
	%_283 = getelementptr i8*, i8** %_282, i32 10
	%_284 = load i8*, i8** %_283
	%_285 = bitcast i8* %_284 to i1 (i8*,i1)*
	%_286 = call i1 %_285(i8* %_280,i1 1)
	store i1 %_286, i1* %ntb
	%_288 = load i8*, i8** %current_node
	%_289 = bitcast i8* %_288 to i8***

	; Tree.SetRight : 1
	%_290 = load i8**, i8*** %_289
	%_291 = getelementptr i8*, i8** %_290, i32 1
	%_292 = load i8*, i8** %_291
	%_293 = bitcast i8* %_292 to i1 (i8*,i8*)*
	%_294 = load i8*, i8** %new_node
	%_295 = call i1 %_293(i8* %_288,i8* %_294)
	store i1 %_295, i1* %ntb
	br label %if17

if17:
	br label %if11

if11:


	br label %loop6

loop8:
	ret i1 1

}

define i1 @Tree.Delete(i8* %this, i32 %.v_key) {
	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key
	%current_node = alloca i8*
	%parent_node = alloca i8*
	%cont = alloca i1
	%found = alloca i1
	%ntb = alloca i1
	%is_root = alloca i1
	%key_aux = alloca i32
	store i8* %this, i8** %current_node
	store i8* %this, i8** %parent_node
	store i1 1, i1* %cont
	store i1 0, i1* %found
	store i1 1, i1* %is_root
	br label %loop18

loop18:
	%_297 = load i1, i1* %cont
	br i1 %_297, label %loop19, label %loop20

loop19:
	%_298 = load i8*, i8** %current_node
	%_299 = bitcast i8* %_298 to i8***

	; Tree.GetKey : 5
	%_300 = load i8**, i8*** %_299
	%_301 = getelementptr i8*, i8** %_300, i32 5
	%_302 = load i8*, i8** %_301
	%_303 = bitcast i8* %_302 to i32 (i8*)*
	%_304 = call i32 %_303(i8* %_298)
	store i32 %_304, i32* %key_aux
	%_305 = load i32, i32* %v_key
	%_306 = load i32, i32* %key_aux
	%_307 = icmp slt i32 %_305, %_306
	br i1 %_307, label %if21, label %if22

if21:
	%_308 = load i8*, i8** %current_node
	%_309 = bitcast i8* %_308 to i8***

	; Tree.GetHas_Left : 8
	%_310 = load i8**, i8*** %_309
	%_311 = getelementptr i8*, i8** %_310, i32 8
	%_312 = load i8*, i8** %_311
	%_313 = bitcast i8* %_312 to i1 (i8*)*
	%_314 = call i1 %_313(i8* %_308)
	br i1 %_314, label %if24, label %if25

if24:
	%_315 = load i8*, i8** %current_node
	store i8* %_315, i8** %parent_node
	%_316 = load i8*, i8** %current_node
	%_317 = bitcast i8* %_316 to i8***

	; Tree.GetLeft : 4
	%_318 = load i8**, i8*** %_317
	%_319 = getelementptr i8*, i8** %_318, i32 4
	%_320 = load i8*, i8** %_319
	%_321 = bitcast i8* %_320 to i8* (i8*)*
	%_322 = call i8* %_321(i8* %_316)
	store i8* %_322, i8** %current_node
	br label %if26

if25:
	store i1 0, i1* %cont
	br label %if26

if26:
	br label %if23

if22:
	%_323 = load i32, i32* %key_aux
	%_324 = load i32, i32* %v_key
	%_325 = icmp slt i32 %_323, %_324
	br i1 %_325, label %if27, label %if28

if27:
	%_326 = load i8*, i8** %current_node
	%_327 = bitcast i8* %_326 to i8***

	; Tree.GetHas_Right : 7
	%_328 = load i8**, i8*** %_327
	%_329 = getelementptr i8*, i8** %_328, i32 7
	%_330 = load i8*, i8** %_329
	%_331 = bitcast i8* %_330 to i1 (i8*)*
	%_332 = call i1 %_331(i8* %_326)
	br i1 %_332, label %if30, label %if31

if30:
	%_333 = load i8*, i8** %current_node
	store i8* %_333, i8** %parent_node
	%_334 = load i8*, i8** %current_node
	%_335 = bitcast i8* %_334 to i8***

	; Tree.GetRight : 3
	%_336 = load i8**, i8*** %_335
	%_337 = getelementptr i8*, i8** %_336, i32 3
	%_338 = load i8*, i8** %_337
	%_339 = bitcast i8* %_338 to i8* (i8*)*
	%_340 = call i8* %_339(i8* %_334)
	store i8* %_340, i8** %current_node
	br label %if32

if31:
	store i1 0, i1* %cont
	br label %if32

if32:
	br label %if29

if28:
	%_341 = load i1, i1* %is_root
	br i1 %_341, label %if33, label %if34

if33:
	%_342 = load i8*, i8** %current_node
	%_343 = bitcast i8* %_342 to i8***

	; Tree.GetHas_Right : 7
	%_344 = load i8**, i8*** %_343
	%_345 = getelementptr i8*, i8** %_344, i32 7
	%_346 = load i8*, i8** %_345
	%_347 = bitcast i8* %_346 to i1 (i8*)*
	%_348 = call i1 %_347(i8* %_342)
	%_349 = xor i1 1, %_348
	br label %andclause36

andclause36:
	br i1 %_349, label %andclause37, label %andclause39

andclause37:
	%_350 = load i8*, i8** %current_node
	%_351 = bitcast i8* %_350 to i8***

	; Tree.GetHas_Left : 8
	%_352 = load i8**, i8*** %_351
	%_353 = getelementptr i8*, i8** %_352, i32 8
	%_354 = load i8*, i8** %_353
	%_355 = bitcast i8* %_354 to i1 (i8*)*
	%_356 = call i1 %_355(i8* %_350)
	%_357 = xor i1 1, %_356
	br label %andclause38

andclause38:
	br label %andclause39

andclause39:
	%_358 = phi i1 [ 0, %andclause36 ], [ %_357, %andclause38 ]
	br i1 %_358, label %if40, label %if41

if40:
	store i1 1, i1* %ntb
	br label %if42

if41:
	%_359 = bitcast i8* %this to i8***

	; Tree.Remove : 14
	%_360 = load i8**, i8*** %_359
	%_361 = getelementptr i8*, i8** %_360, i32 14
	%_362 = load i8*, i8** %_361
	%_363 = bitcast i8* %_362 to i1 (i8*,i8*,i8*)*
	%_364 = load i8*, i8** %parent_node
	%_365 = load i8*, i8** %current_node
	%_366 = call i1 %_363(i8* %this,i8* %_364,i8* %_365)
	store i1 %_366, i1* %ntb
	br label %if42

if42:
	br label %if35

if34:
	%_369 = bitcast i8* %this to i8***

	; Tree.Remove : 14
	%_370 = load i8**, i8*** %_369
	%_371 = getelementptr i8*, i8** %_370, i32 14
	%_372 = load i8*, i8** %_371
	%_373 = bitcast i8* %_372 to i1 (i8*,i8*,i8*)*
	%_374 = load i8*, i8** %parent_node
	%_375 = load i8*, i8** %current_node
	%_376 = call i1 %_373(i8* %this,i8* %_374,i8* %_375)
	store i1 %_376, i1* %ntb
	br label %if35

if35:
	store i1 1, i1* %found
	store i1 0, i1* %cont
	br label %if29

if29:
	br label %if23

if23:
	store i1 0, i1* %is_root


	br label %loop18

loop20:
	%_379 = load i1, i1* %found
	ret i1 %_379

}

define i1 @Tree.Remove(i8* %this, i8* %.p_node, i8* %.c_node) {
	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node
	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node
	%ntb = alloca i1
	%auxkey1 = alloca i32
	%auxkey2 = alloca i32
	%_380 = load i8*, i8** %c_node
	%_381 = bitcast i8* %_380 to i8***

	; Tree.GetHas_Left : 8
	%_382 = load i8**, i8*** %_381
	%_383 = getelementptr i8*, i8** %_382, i32 8
	%_384 = load i8*, i8** %_383
	%_385 = bitcast i8* %_384 to i1 (i8*)*
	%_386 = call i1 %_385(i8* %_380)
	br i1 %_386, label %if43, label %if44

if43:
	%_387 = bitcast i8* %this to i8***

	; Tree.RemoveLeft : 16
	%_388 = load i8**, i8*** %_387
	%_389 = getelementptr i8*, i8** %_388, i32 16
	%_390 = load i8*, i8** %_389
	%_391 = bitcast i8* %_390 to i1 (i8*,i8*,i8*)*
	%_392 = load i8*, i8** %p_node
	%_393 = load i8*, i8** %c_node
	%_394 = call i1 %_391(i8* %this,i8* %_392,i8* %_393)
	store i1 %_394, i1* %ntb
	br label %if45

if44:
	%_397 = load i8*, i8** %c_node
	%_398 = bitcast i8* %_397 to i8***

	; Tree.GetHas_Right : 7
	%_399 = load i8**, i8*** %_398
	%_400 = getelementptr i8*, i8** %_399, i32 7
	%_401 = load i8*, i8** %_400
	%_402 = bitcast i8* %_401 to i1 (i8*)*
	%_403 = call i1 %_402(i8* %_397)
	br i1 %_403, label %if46, label %if47

if46:
	%_404 = bitcast i8* %this to i8***

	; Tree.RemoveRight : 15
	%_405 = load i8**, i8*** %_404
	%_406 = getelementptr i8*, i8** %_405, i32 15
	%_407 = load i8*, i8** %_406
	%_408 = bitcast i8* %_407 to i1 (i8*,i8*,i8*)*
	%_409 = load i8*, i8** %p_node
	%_410 = load i8*, i8** %c_node
	%_411 = call i1 %_408(i8* %this,i8* %_409,i8* %_410)
	store i1 %_411, i1* %ntb
	br label %if48

if47:
	%_414 = load i8*, i8** %c_node
	%_415 = bitcast i8* %_414 to i8***

	; Tree.GetKey : 5
	%_416 = load i8**, i8*** %_415
	%_417 = getelementptr i8*, i8** %_416, i32 5
	%_418 = load i8*, i8** %_417
	%_419 = bitcast i8* %_418 to i32 (i8*)*
	%_420 = call i32 %_419(i8* %_414)
	store i32 %_420, i32* %auxkey1
	%_421 = load i8*, i8** %p_node
	%_422 = bitcast i8* %_421 to i8***

	; Tree.GetLeft : 4
	%_423 = load i8**, i8*** %_422
	%_424 = getelementptr i8*, i8** %_423, i32 4
	%_425 = load i8*, i8** %_424
	%_426 = bitcast i8* %_425 to i8* (i8*)*
	%_427 = call i8* %_426(i8* %_421)
	%_428 = bitcast i8* %_427 to i8***

	; Tree.GetKey : 5
	%_429 = load i8**, i8*** %_428
	%_430 = getelementptr i8*, i8** %_429, i32 5
	%_431 = load i8*, i8** %_430
	%_432 = bitcast i8* %_431 to i32 (i8*)*
	%_433 = call i32 %_432(i8* %_427)
	store i32 %_433, i32* %auxkey2
	%_434 = bitcast i8* %this to i8***

	; Tree.Compare : 11
	%_435 = load i8**, i8*** %_434
	%_436 = getelementptr i8*, i8** %_435, i32 11
	%_437 = load i8*, i8** %_436
	%_438 = bitcast i8* %_437 to i1 (i8*,i32,i32)*
	%_439 = load i32, i32* %auxkey1
	%_440 = load i32, i32* %auxkey2
	%_441 = call i1 %_438(i8* %this,i32 %_439,i32 %_440)
	br i1 %_441, label %if49, label %if50

if49:
	%_444 = load i8*, i8** %p_node
	%_445 = bitcast i8* %_444 to i8***

	; Tree.SetLeft : 2
	%_446 = load i8**, i8*** %_445
	%_447 = getelementptr i8*, i8** %_446, i32 2
	%_448 = load i8*, i8** %_447
	%_449 = bitcast i8* %_448 to i1 (i8*,i8*)*
	%_450 = getelementptr i8, i8* %this , i32 30
	%_451 = bitcast i8* %_450 to i8**
	%_452 = load i8*, i8** %_451
	%_453 = call i1 %_449(i8* %_444,i8* %_452)
	store i1 %_453, i1* %ntb
	%_455 = load i8*, i8** %p_node
	%_456 = bitcast i8* %_455 to i8***

	; Tree.SetHas_Left : 9
	%_457 = load i8**, i8*** %_456
	%_458 = getelementptr i8*, i8** %_457, i32 9
	%_459 = load i8*, i8** %_458
	%_460 = bitcast i8* %_459 to i1 (i8*,i1)*
	%_461 = call i1 %_460(i8* %_455,i1 0)
	store i1 %_461, i1* %ntb
	br label %if51

if50:
	%_463 = load i8*, i8** %p_node
	%_464 = bitcast i8* %_463 to i8***

	; Tree.SetRight : 1
	%_465 = load i8**, i8*** %_464
	%_466 = getelementptr i8*, i8** %_465, i32 1
	%_467 = load i8*, i8** %_466
	%_468 = bitcast i8* %_467 to i1 (i8*,i8*)*
	%_469 = getelementptr i8, i8* %this , i32 30
	%_470 = bitcast i8* %_469 to i8**
	%_471 = load i8*, i8** %_470
	%_472 = call i1 %_468(i8* %_463,i8* %_471)
	store i1 %_472, i1* %ntb
	%_474 = load i8*, i8** %p_node
	%_475 = bitcast i8* %_474 to i8***

	; Tree.SetHas_Right : 10
	%_476 = load i8**, i8*** %_475
	%_477 = getelementptr i8*, i8** %_476, i32 10
	%_478 = load i8*, i8** %_477
	%_479 = bitcast i8* %_478 to i1 (i8*,i1)*
	%_480 = call i1 %_479(i8* %_474,i1 0)
	store i1 %_480, i1* %ntb
	br label %if51

if51:
	br label %if48

if48:
	br label %if45

if45:
	ret i1 1

}

define i1 @Tree.RemoveRight(i8* %this, i8* %.p_node, i8* %.c_node) {
	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node
	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node
	%ntb = alloca i1
	br label %loop52

loop52:
	%_482 = load i8*, i8** %c_node
	%_483 = bitcast i8* %_482 to i8***

	; Tree.GetHas_Right : 7
	%_484 = load i8**, i8*** %_483
	%_485 = getelementptr i8*, i8** %_484, i32 7
	%_486 = load i8*, i8** %_485
	%_487 = bitcast i8* %_486 to i1 (i8*)*
	%_488 = call i1 %_487(i8* %_482)
	br i1 %_488, label %loop53, label %loop54

loop53:
	%_489 = load i8*, i8** %c_node
	%_490 = bitcast i8* %_489 to i8***

	; Tree.SetKey : 6
	%_491 = load i8**, i8*** %_490
	%_492 = getelementptr i8*, i8** %_491, i32 6
	%_493 = load i8*, i8** %_492
	%_494 = bitcast i8* %_493 to i1 (i8*,i32)*
	%_495 = load i8*, i8** %c_node
	%_496 = bitcast i8* %_495 to i8***

	; Tree.GetRight : 3
	%_497 = load i8**, i8*** %_496
	%_498 = getelementptr i8*, i8** %_497, i32 3
	%_499 = load i8*, i8** %_498
	%_500 = bitcast i8* %_499 to i8* (i8*)*
	%_501 = call i8* %_500(i8* %_495)
	%_502 = bitcast i8* %_501 to i8***

	; Tree.GetKey : 5
	%_503 = load i8**, i8*** %_502
	%_504 = getelementptr i8*, i8** %_503, i32 5
	%_505 = load i8*, i8** %_504
	%_506 = bitcast i8* %_505 to i32 (i8*)*
	%_507 = call i32 %_506(i8* %_501)
	%_508 = call i1 %_494(i8* %_489,i32 %_507)
	store i1 %_508, i1* %ntb
	%_510 = load i8*, i8** %c_node
	store i8* %_510, i8** %p_node
	%_511 = load i8*, i8** %c_node
	%_512 = bitcast i8* %_511 to i8***

	; Tree.GetRight : 3
	%_513 = load i8**, i8*** %_512
	%_514 = getelementptr i8*, i8** %_513, i32 3
	%_515 = load i8*, i8** %_514
	%_516 = bitcast i8* %_515 to i8* (i8*)*
	%_517 = call i8* %_516(i8* %_511)
	store i8* %_517, i8** %c_node


	br label %loop52

loop54:
	%_518 = load i8*, i8** %p_node
	%_519 = bitcast i8* %_518 to i8***

	; Tree.SetRight : 1
	%_520 = load i8**, i8*** %_519
	%_521 = getelementptr i8*, i8** %_520, i32 1
	%_522 = load i8*, i8** %_521
	%_523 = bitcast i8* %_522 to i1 (i8*,i8*)*
	%_524 = getelementptr i8, i8* %this , i32 30
	%_525 = bitcast i8* %_524 to i8**
	%_526 = load i8*, i8** %_525
	%_527 = call i1 %_523(i8* %_518,i8* %_526)
	store i1 %_527, i1* %ntb
	%_529 = load i8*, i8** %p_node
	%_530 = bitcast i8* %_529 to i8***

	; Tree.SetHas_Right : 10
	%_531 = load i8**, i8*** %_530
	%_532 = getelementptr i8*, i8** %_531, i32 10
	%_533 = load i8*, i8** %_532
	%_534 = bitcast i8* %_533 to i1 (i8*,i1)*
	%_535 = call i1 %_534(i8* %_529,i1 0)
	store i1 %_535, i1* %ntb
	ret i1 1

}

define i1 @Tree.RemoveLeft(i8* %this, i8* %.p_node, i8* %.c_node) {
	%p_node = alloca i8*
	store i8* %.p_node, i8** %p_node
	%c_node = alloca i8*
	store i8* %.c_node, i8** %c_node
	%ntb = alloca i1
	br label %loop55

loop55:
	%_537 = load i8*, i8** %c_node
	%_538 = bitcast i8* %_537 to i8***

	; Tree.GetHas_Left : 8
	%_539 = load i8**, i8*** %_538
	%_540 = getelementptr i8*, i8** %_539, i32 8
	%_541 = load i8*, i8** %_540
	%_542 = bitcast i8* %_541 to i1 (i8*)*
	%_543 = call i1 %_542(i8* %_537)
	br i1 %_543, label %loop56, label %loop57

loop56:
	%_544 = load i8*, i8** %c_node
	%_545 = bitcast i8* %_544 to i8***

	; Tree.SetKey : 6
	%_546 = load i8**, i8*** %_545
	%_547 = getelementptr i8*, i8** %_546, i32 6
	%_548 = load i8*, i8** %_547
	%_549 = bitcast i8* %_548 to i1 (i8*,i32)*
	%_550 = load i8*, i8** %c_node
	%_551 = bitcast i8* %_550 to i8***

	; Tree.GetLeft : 4
	%_552 = load i8**, i8*** %_551
	%_553 = getelementptr i8*, i8** %_552, i32 4
	%_554 = load i8*, i8** %_553
	%_555 = bitcast i8* %_554 to i8* (i8*)*
	%_556 = call i8* %_555(i8* %_550)
	%_557 = bitcast i8* %_556 to i8***

	; Tree.GetKey : 5
	%_558 = load i8**, i8*** %_557
	%_559 = getelementptr i8*, i8** %_558, i32 5
	%_560 = load i8*, i8** %_559
	%_561 = bitcast i8* %_560 to i32 (i8*)*
	%_562 = call i32 %_561(i8* %_556)
	%_563 = call i1 %_549(i8* %_544,i32 %_562)
	store i1 %_563, i1* %ntb
	%_565 = load i8*, i8** %c_node
	store i8* %_565, i8** %p_node
	%_566 = load i8*, i8** %c_node
	%_567 = bitcast i8* %_566 to i8***

	; Tree.GetLeft : 4
	%_568 = load i8**, i8*** %_567
	%_569 = getelementptr i8*, i8** %_568, i32 4
	%_570 = load i8*, i8** %_569
	%_571 = bitcast i8* %_570 to i8* (i8*)*
	%_572 = call i8* %_571(i8* %_566)
	store i8* %_572, i8** %c_node


	br label %loop55

loop57:
	%_573 = load i8*, i8** %p_node
	%_574 = bitcast i8* %_573 to i8***

	; Tree.SetLeft : 2
	%_575 = load i8**, i8*** %_574
	%_576 = getelementptr i8*, i8** %_575, i32 2
	%_577 = load i8*, i8** %_576
	%_578 = bitcast i8* %_577 to i1 (i8*,i8*)*
	%_579 = getelementptr i8, i8* %this , i32 30
	%_580 = bitcast i8* %_579 to i8**
	%_581 = load i8*, i8** %_580
	%_582 = call i1 %_578(i8* %_573,i8* %_581)
	store i1 %_582, i1* %ntb
	%_584 = load i8*, i8** %p_node
	%_585 = bitcast i8* %_584 to i8***

	; Tree.SetHas_Left : 9
	%_586 = load i8**, i8*** %_585
	%_587 = getelementptr i8*, i8** %_586, i32 9
	%_588 = load i8*, i8** %_587
	%_589 = bitcast i8* %_588 to i1 (i8*,i1)*
	%_590 = call i1 %_589(i8* %_584,i1 0)
	store i1 %_590, i1* %ntb
	ret i1 1

}

define i32 @Tree.Search(i8* %this, i32 %.v_key) {
	%v_key = alloca i32
	store i32 %.v_key, i32* %v_key
	%current_node = alloca i8*
	%ifound = alloca i32
	%cont = alloca i1
	%key_aux = alloca i32
	store i8* %this, i8** %current_node
	store i1 1, i1* %cont
	store i32 0, i32* %ifound
	br label %loop58

loop58:
	%_592 = load i1, i1* %cont
	br i1 %_592, label %loop59, label %loop60

loop59:
	%_593 = load i8*, i8** %current_node
	%_594 = bitcast i8* %_593 to i8***

	; Tree.GetKey : 5
	%_595 = load i8**, i8*** %_594
	%_596 = getelementptr i8*, i8** %_595, i32 5
	%_597 = load i8*, i8** %_596
	%_598 = bitcast i8* %_597 to i32 (i8*)*
	%_599 = call i32 %_598(i8* %_593)
	store i32 %_599, i32* %key_aux
	%_600 = load i32, i32* %v_key
	%_601 = load i32, i32* %key_aux
	%_602 = icmp slt i32 %_600, %_601
	br i1 %_602, label %if61, label %if62

if61:
	%_603 = load i8*, i8** %current_node
	%_604 = bitcast i8* %_603 to i8***

	; Tree.GetHas_Left : 8
	%_605 = load i8**, i8*** %_604
	%_606 = getelementptr i8*, i8** %_605, i32 8
	%_607 = load i8*, i8** %_606
	%_608 = bitcast i8* %_607 to i1 (i8*)*
	%_609 = call i1 %_608(i8* %_603)
	br i1 %_609, label %if64, label %if65

if64:
	%_610 = load i8*, i8** %current_node
	%_611 = bitcast i8* %_610 to i8***

	; Tree.GetLeft : 4
	%_612 = load i8**, i8*** %_611
	%_613 = getelementptr i8*, i8** %_612, i32 4
	%_614 = load i8*, i8** %_613
	%_615 = bitcast i8* %_614 to i8* (i8*)*
	%_616 = call i8* %_615(i8* %_610)
	store i8* %_616, i8** %current_node
	br label %if66

if65:
	store i1 0, i1* %cont
	br label %if66

if66:
	br label %if63

if62:
	%_617 = load i32, i32* %key_aux
	%_618 = load i32, i32* %v_key
	%_619 = icmp slt i32 %_617, %_618
	br i1 %_619, label %if67, label %if68

if67:
	%_620 = load i8*, i8** %current_node
	%_621 = bitcast i8* %_620 to i8***

	; Tree.GetHas_Right : 7
	%_622 = load i8**, i8*** %_621
	%_623 = getelementptr i8*, i8** %_622, i32 7
	%_624 = load i8*, i8** %_623
	%_625 = bitcast i8* %_624 to i1 (i8*)*
	%_626 = call i1 %_625(i8* %_620)
	br i1 %_626, label %if70, label %if71

if70:
	%_627 = load i8*, i8** %current_node
	%_628 = bitcast i8* %_627 to i8***

	; Tree.GetRight : 3
	%_629 = load i8**, i8*** %_628
	%_630 = getelementptr i8*, i8** %_629, i32 3
	%_631 = load i8*, i8** %_630
	%_632 = bitcast i8* %_631 to i8* (i8*)*
	%_633 = call i8* %_632(i8* %_627)
	store i8* %_633, i8** %current_node
	br label %if72

if71:
	store i1 0, i1* %cont
	br label %if72

if72:
	br label %if69

if68:
	store i32 1, i32* %ifound
	store i1 0, i1* %cont
	br label %if69

if69:
	br label %if63

if63:


	br label %loop58

loop60:
	%_634 = load i32, i32* %ifound
	ret i32 %_634

}

define i1 @Tree.Print(i8* %this) {
	%ntb = alloca i1
	%current_node = alloca i8*
	store i8* %this, i8** %current_node
	%_635 = bitcast i8* %this to i8***

	; Tree.RecPrint : 19
	%_636 = load i8**, i8*** %_635
	%_637 = getelementptr i8*, i8** %_636, i32 19
	%_638 = load i8*, i8** %_637
	%_639 = bitcast i8* %_638 to i1 (i8*,i8*)*
	%_640 = load i8*, i8** %current_node
	%_641 = call i1 %_639(i8* %this,i8* %_640)
	store i1 %_641, i1* %ntb
	ret i1 1

}

define i1 @Tree.RecPrint(i8* %this, i8* %.node) {
	%node = alloca i8*
	store i8* %.node, i8** %node
	%ntb = alloca i1
	%_643 = load i8*, i8** %node
	%_644 = bitcast i8* %_643 to i8***

	; Tree.GetHas_Left : 8
	%_645 = load i8**, i8*** %_644
	%_646 = getelementptr i8*, i8** %_645, i32 8
	%_647 = load i8*, i8** %_646
	%_648 = bitcast i8* %_647 to i1 (i8*)*
	%_649 = call i1 %_648(i8* %_643)
	br i1 %_649, label %if73, label %if74

if73:
	%_650 = bitcast i8* %this to i8***

	; Tree.RecPrint : 19
	%_651 = load i8**, i8*** %_650
	%_652 = getelementptr i8*, i8** %_651, i32 19
	%_653 = load i8*, i8** %_652
	%_654 = bitcast i8* %_653 to i1 (i8*,i8*)*
	%_655 = load i8*, i8** %node
	%_656 = bitcast i8* %_655 to i8***

	; Tree.GetLeft : 4
	%_657 = load i8**, i8*** %_656
	%_658 = getelementptr i8*, i8** %_657, i32 4
	%_659 = load i8*, i8** %_658
	%_660 = bitcast i8* %_659 to i8* (i8*)*
	%_661 = call i8* %_660(i8* %_655)
	%_662 = call i1 %_654(i8* %this,i8* %_661)
	store i1 %_662, i1* %ntb
	br label %if75

if74:
	store i1 1, i1* %ntb
	br label %if75

if75:
	%_664 = load i8*, i8** %node
	%_665 = bitcast i8* %_664 to i8***

	; Tree.GetKey : 5
	%_666 = load i8**, i8*** %_665
	%_667 = getelementptr i8*, i8** %_666, i32 5
	%_668 = load i8*, i8** %_667
	%_669 = bitcast i8* %_668 to i32 (i8*)*
	%_670 = call i32 %_669(i8* %_664)

	call void (i32) @print_int(i32 %_670)
	%_671 = load i8*, i8** %node
	%_672 = bitcast i8* %_671 to i8***

	; Tree.GetHas_Right : 7
	%_673 = load i8**, i8*** %_672
	%_674 = getelementptr i8*, i8** %_673, i32 7
	%_675 = load i8*, i8** %_674
	%_676 = bitcast i8* %_675 to i1 (i8*)*
	%_677 = call i1 %_676(i8* %_671)
	br i1 %_677, label %if76, label %if77

if76:
	%_678 = bitcast i8* %this to i8***

	; Tree.RecPrint : 19
	%_679 = load i8**, i8*** %_678
	%_680 = getelementptr i8*, i8** %_679, i32 19
	%_681 = load i8*, i8** %_680
	%_682 = bitcast i8* %_681 to i1 (i8*,i8*)*
	%_683 = load i8*, i8** %node
	%_684 = bitcast i8* %_683 to i8***

	; Tree.GetRight : 3
	%_685 = load i8**, i8*** %_684
	%_686 = getelementptr i8*, i8** %_685, i32 3
	%_687 = load i8*, i8** %_686
	%_688 = bitcast i8* %_687 to i8* (i8*)*
	%_689 = call i8* %_688(i8* %_683)
	%_690 = call i1 %_682(i8* %this,i8* %_689)
	store i1 %_690, i1* %ntb
	br label %if78

if77:
	store i1 1, i1* %ntb
	br label %if78

if78:
	ret i1 1

}

define i32 @Tree.accept(i8* %this, i8* %.v) {
	%v = alloca i8*
	store i8* %.v, i8** %v
	%nti = alloca i32

	call void (i32) @print_int(i32 333)
	%_692 = load i8*, i8** %v
	%_693 = bitcast i8* %_692 to i8***

	; Visitor.visit : 0
	%_694 = load i8**, i8*** %_693
	%_695 = getelementptr i8*, i8** %_694, i32 0
	%_696 = load i8*, i8** %_695
	%_697 = bitcast i8* %_696 to i32 (i8*,i8*)*
	%_698 = call i32 %_697(i8* %_692,i8* %this)
	store i32 %_698, i32* %nti
	ret i32 0

}

define i32 @Visitor.visit(i8* %this, i8* %.n) {
	%n = alloca i8*
	store i8* %.n, i8** %n
	%nti = alloca i32
	%_700 = load i8*, i8** %n
	%_701 = bitcast i8* %_700 to i8***

	; Tree.GetHas_Right : 7
	%_702 = load i8**, i8*** %_701
	%_703 = getelementptr i8*, i8** %_702, i32 7
	%_704 = load i8*, i8** %_703
	%_705 = bitcast i8* %_704 to i1 (i8*)*
	%_706 = call i1 %_705(i8* %_700)
	br i1 %_706, label %if79, label %if80

if79:
	%_707 = load i8*, i8** %n
	%_708 = bitcast i8* %_707 to i8***

	; Tree.GetRight : 3
	%_709 = load i8**, i8*** %_708
	%_710 = getelementptr i8*, i8** %_709, i32 3
	%_711 = load i8*, i8** %_710
	%_712 = bitcast i8* %_711 to i8* (i8*)*
	%_713 = call i8* %_712(i8* %_707)
	%_714 = getelementptr i8, i8* %this , i32 16
	%_715 = bitcast i8* %_714 to i8**
	store i8* %_713, i8** %_715
	%_716 = getelementptr i8, i8* %this , i32 16
	%_717 = bitcast i8* %_716 to i8**
	%_718 = load i8*, i8** %_717
	%_719 = bitcast i8* %_718 to i8***

	; Tree.accept : 20
	%_720 = load i8**, i8*** %_719
	%_721 = getelementptr i8*, i8** %_720, i32 20
	%_722 = load i8*, i8** %_721
	%_723 = bitcast i8* %_722 to i32 (i8*,i8*)*
	%_724 = call i32 %_723(i8* %_718,i8* %this)
	store i32 %_724, i32* %nti
	br label %if81

if80:
	store i32 0, i32* %nti
	br label %if81

if81:
	%_726 = load i8*, i8** %n
	%_727 = bitcast i8* %_726 to i8***

	; Tree.GetHas_Left : 8
	%_728 = load i8**, i8*** %_727
	%_729 = getelementptr i8*, i8** %_728, i32 8
	%_730 = load i8*, i8** %_729
	%_731 = bitcast i8* %_730 to i1 (i8*)*
	%_732 = call i1 %_731(i8* %_726)
	br i1 %_732, label %if82, label %if83

if82:
	%_733 = load i8*, i8** %n
	%_734 = bitcast i8* %_733 to i8***

	; Tree.GetLeft : 4
	%_735 = load i8**, i8*** %_734
	%_736 = getelementptr i8*, i8** %_735, i32 4
	%_737 = load i8*, i8** %_736
	%_738 = bitcast i8* %_737 to i8* (i8*)*
	%_739 = call i8* %_738(i8* %_733)
	%_740 = getelementptr i8, i8* %this , i32 8
	%_741 = bitcast i8* %_740 to i8**
	store i8* %_739, i8** %_741
	%_742 = getelementptr i8, i8* %this , i32 8
	%_743 = bitcast i8* %_742 to i8**
	%_744 = load i8*, i8** %_743
	%_745 = bitcast i8* %_744 to i8***

	; Tree.accept : 20
	%_746 = load i8**, i8*** %_745
	%_747 = getelementptr i8*, i8** %_746, i32 20
	%_748 = load i8*, i8** %_747
	%_749 = bitcast i8* %_748 to i32 (i8*,i8*)*
	%_750 = call i32 %_749(i8* %_744,i8* %this)
	store i32 %_750, i32* %nti
	br label %if84

if83:
	store i32 0, i32* %nti
	br label %if84

if84:
	ret i32 0

}

define i32 @MyVisitor.visit(i8* %this, i8* %.n) {
	%n = alloca i8*
	store i8* %.n, i8** %n
	%nti = alloca i32
	%_752 = load i8*, i8** %n
	%_753 = bitcast i8* %_752 to i8***

	; Tree.GetHas_Right : 7
	%_754 = load i8**, i8*** %_753
	%_755 = getelementptr i8*, i8** %_754, i32 7
	%_756 = load i8*, i8** %_755
	%_757 = bitcast i8* %_756 to i1 (i8*)*
	%_758 = call i1 %_757(i8* %_752)
	br i1 %_758, label %if85, label %if86

if85:
	%_759 = load i8*, i8** %n
	%_760 = bitcast i8* %_759 to i8***

	; Tree.GetRight : 3
	%_761 = load i8**, i8*** %_760
	%_762 = getelementptr i8*, i8** %_761, i32 3
	%_763 = load i8*, i8** %_762
	%_764 = bitcast i8* %_763 to i8* (i8*)*
	%_765 = call i8* %_764(i8* %_759)
	%_766 = getelementptr i8, i8* %this , i32 16
	%_767 = bitcast i8* %_766 to i8**
	store i8* %_765, i8** %_767
	%_768 = getelementptr i8, i8* %this , i32 16
	%_769 = bitcast i8* %_768 to i8**
	%_770 = load i8*, i8** %_769
	%_771 = bitcast i8* %_770 to i8***

	; Tree.accept : 20
	%_772 = load i8**, i8*** %_771
	%_773 = getelementptr i8*, i8** %_772, i32 20
	%_774 = load i8*, i8** %_773
	%_775 = bitcast i8* %_774 to i32 (i8*,i8*)*
	%_776 = call i32 %_775(i8* %_770,i8* %this)
	store i32 %_776, i32* %nti
	br label %if87

if86:
	store i32 0, i32* %nti
	br label %if87

if87:
	%_778 = load i8*, i8** %n
	%_779 = bitcast i8* %_778 to i8***

	; Tree.GetKey : 5
	%_780 = load i8**, i8*** %_779
	%_781 = getelementptr i8*, i8** %_780, i32 5
	%_782 = load i8*, i8** %_781
	%_783 = bitcast i8* %_782 to i32 (i8*)*
	%_784 = call i32 %_783(i8* %_778)

	call void (i32) @print_int(i32 %_784)
	%_785 = load i8*, i8** %n
	%_786 = bitcast i8* %_785 to i8***

	; Tree.GetHas_Left : 8
	%_787 = load i8**, i8*** %_786
	%_788 = getelementptr i8*, i8** %_787, i32 8
	%_789 = load i8*, i8** %_788
	%_790 = bitcast i8* %_789 to i1 (i8*)*
	%_791 = call i1 %_790(i8* %_785)
	br i1 %_791, label %if88, label %if89

if88:
	%_792 = load i8*, i8** %n
	%_793 = bitcast i8* %_792 to i8***

	; Tree.GetLeft : 4
	%_794 = load i8**, i8*** %_793
	%_795 = getelementptr i8*, i8** %_794, i32 4
	%_796 = load i8*, i8** %_795
	%_797 = bitcast i8* %_796 to i8* (i8*)*
	%_798 = call i8* %_797(i8* %_792)
	%_799 = getelementptr i8, i8* %this , i32 8
	%_800 = bitcast i8* %_799 to i8**
	store i8* %_798, i8** %_800
	%_801 = getelementptr i8, i8* %this , i32 8
	%_802 = bitcast i8* %_801 to i8**
	%_803 = load i8*, i8** %_802
	%_804 = bitcast i8* %_803 to i8***

	; Tree.accept : 20
	%_805 = load i8**, i8*** %_804
	%_806 = getelementptr i8*, i8** %_805, i32 20
	%_807 = load i8*, i8** %_806
	%_808 = bitcast i8* %_807 to i32 (i8*,i8*)*
	%_809 = call i32 %_808(i8* %_803,i8* %this)
	store i32 %_809, i32* %nti
	br label %if90

if89:
	store i32 0, i32* %nti
	br label %if90

if90:
	ret i32 0

}