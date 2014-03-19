package br.josepedro.chordsdetector;

public class MyFFT {

	private int n, m;
	private float[] cos;
	private float[] sin;
	private float[] somFFT;
	private float[] Som;
	private float fs = 44100;
	private float[] f;
	private float[] respfreq;
	private float[] respFreq;
	private static float[][] notas = new float[12][1986];
	private float[] S1 = new float[12];
	private static float[][] acordes = new float[48][12];
	private float[] S2 = new float[48];
	// private static String[] nomeAcordes = { "C M", "C m", "C aum", "C dim",
	// "C# M", "C# m", "C# aum", "C# dim", "D M", "D m", "D aum", "D dim",
	// "Eb M", "Eb m", "Eb aum", "Eb dim", "E M", "E m", "E aum", "E dim",
	// "F M", "F m", "F aum", "F dim", "F# M", "F# m", "F# aum", "F# dim",
	// "G M", "G m", "G aum", "G dim", "G# M", "G# m", "G# aum", "G# dim",
	// "A M", "A m", "A aum", "A dim", "Bb M", "Bb m", "Bb aum", "Bb dim",
	// "B M", "B m", "B aum", "B dim" };
	private static String[] nomeAcordes = { "F", "F m", "F aum", "F dim", "F#",
			"F# m", "F# aum", "F# dim", "G", "G m", "G aum", "G dim", "G#",
			"G# m", "G# aum", "G# dim", "A", "A m", "A aum", "A dim", "Bb",
			"Bb m", "Bb aum", "Bb dim", "B", "B m", "B aum", "B dim", "C",
			"C m", "C aum", "C dim", "C#", "C# m", "C# aum", "C# dim", "D",
			"D m", "D aum", "D dim", "Eb", "Eb m", "Eb aum", "Eb dim", "E",
			"E m", "E aum", "E dim" };
	private String Acorde;
	private byte[] songInByte;
	private float[] songInFloat;
	private float energy;
	
	public MyFFT() {

		// Preenchendo a matriz notas por 0's
		for (int linha = 0; linha < 12; linha++) {
			for (int coluna = 0; coluna < 1986; coluna++) {
				notas[linha][coluna] = 0;
			}
		}

		float g1 = (float) 0.01;
		float g2 = (float) 0.05;
		float g3 = (float) 0.1;
		float g4 = (float) 0.4;
		float g5 = (float) 0.8;
		float g6 = 1;

		/** Linha[0] = Dó */
		notas[0][60] = g1;
		notas[0][61] = g2;
		notas[0][62] = g3;
		notas[0][63] = g4;
		notas[0][64] = g5;
		notas[0][65] = g6;
		notas[0][66] = g5;
		notas[0][67] = g4;
		notas[0][68] = g3;
		notas[0][69] = g2;
		notas[0][70] = g1;

		notas[0][126] = g1;
		notas[0][127] = g2;
		notas[0][128] = g3;
		notas[0][129] = g4;
		notas[0][130] = g5;
		notas[0][131] = g6;
		notas[0][132] = g5;
		notas[0][133] = g4;
		notas[0][134] = g3;
		notas[0][135] = g2;
		notas[0][136] = g1;

		notas[0][256] = g1;
		notas[0][257] = g2;
		notas[0][258] = g3;
		notas[0][259] = g4;
		notas[0][260] = g5;
		notas[0][261] = g6;
		notas[0][262] = g5;
		notas[0][263] = g4;
		notas[0][264] = g3;
		notas[0][265] = g2;
		notas[0][266] = g1;

		notas[0][518] = g1;
		notas[0][519] = g2;
		notas[0][520] = g3;
		notas[0][521] = g4;
		notas[0][522] = g5;
		notas[0][523] = g6;
		notas[0][524] = g5;
		notas[0][525] = g4;
		notas[0][526] = g3;
		notas[0][527] = g2;
		notas[0][528] = g1;

		notas[0][1041] = g1;
		notas[0][1042] = g2;
		notas[0][1043] = g3;
		notas[0][1044] = g4;
		notas[0][1045] = g5;
		notas[0][1046] = g6;
		notas[0][1047] = g5;
		notas[0][1048] = g4;
		notas[0][1049] = g3;
		notas[0][1050] = g2;
		notas[0][1051] = g1;

		/** Linha[1] = Dó# */
		notas[1][64] = g1;
		notas[1][65] = g2;
		notas[1][66] = g3;
		notas[1][67] = g4;
		notas[1][68] = g5;
		notas[1][69] = g6;
		notas[1][70] = g5;
		notas[1][71] = g4;
		notas[1][72] = g3;
		notas[1][73] = g2;
		notas[1][74] = g1;

		notas[1][133] = g1;
		notas[1][134] = g2;
		notas[1][135] = g3;
		notas[1][136] = g4;
		notas[1][137] = g5;
		notas[1][138] = g6;
		notas[1][139] = g5;
		notas[1][140] = g4;
		notas[1][141] = g3;
		notas[1][142] = g2;
		notas[1][143] = g1;

		notas[1][272] = g1;
		notas[1][273] = g2;
		notas[1][274] = g3;
		notas[1][275] = g4;
		notas[1][276] = g5;
		notas[1][277] = g6;
		notas[1][278] = g5;
		notas[1][279] = g4;
		notas[1][280] = g3;
		notas[1][281] = g2;
		notas[1][282] = g1;

		notas[1][549] = g1;
		notas[1][550] = g2;
		notas[1][551] = g3;
		notas[1][552] = g4;
		notas[1][553] = g5;
		notas[1][554] = g6;
		notas[1][555] = g5;
		notas[1][556] = g4;
		notas[1][557] = g3;
		notas[1][558] = g2;
		notas[1][559] = g1;

		notas[1][1104] = g1;
		notas[1][1105] = g2;
		notas[1][1106] = g3;
		notas[1][1107] = g4;
		notas[1][1108] = g5;
		notas[1][1109] = g6;
		notas[1][1110] = g5;
		notas[1][1111] = g4;
		notas[1][1112] = g3;
		notas[1][1113] = g2;
		notas[1][1114] = g1;

		/** Linha[2] = Ré */
		notas[2][68] = g1;
		notas[2][69] = g2;
		notas[2][70] = g3;
		notas[2][71] = g4;
		notas[2][72] = g5;
		notas[2][73] = g6;
		notas[2][74] = g5;
		notas[2][75] = g4;
		notas[2][76] = g3;
		notas[2][77] = g2;
		notas[2][78] = g1;

		notas[2][142] = g1;
		notas[2][143] = g2;
		notas[2][144] = g3;
		notas[2][145] = g4;
		notas[2][146] = g5;
		notas[2][147] = g6;
		notas[2][148] = g5;
		notas[2][149] = g4;
		notas[2][150] = g3;
		notas[2][151] = g2;
		notas[2][152] = g1;

		notas[2][289] = g1;
		notas[2][290] = g2;
		notas[2][291] = g3;
		notas[2][292] = g4;
		notas[2][293] = g5;
		notas[2][294] = g6;
		notas[2][295] = g5;
		notas[2][296] = g4;
		notas[2][297] = g3;
		notas[2][298] = g2;
		notas[2][299] = g1;

		notas[2][582] = g1;
		notas[2][583] = g2;
		notas[2][584] = g3;
		notas[2][585] = g4;
		notas[2][586] = g5;
		notas[2][587] = g6;
		notas[2][588] = g5;
		notas[2][589] = g4;
		notas[2][590] = g3;
		notas[2][591] = g2;
		notas[2][592] = g1;

		notas[2][1169] = g1;
		notas[2][1170] = g2;
		notas[2][1171] = g3;
		notas[2][1172] = g4;
		notas[2][1173] = g5;
		notas[2][1174] = g6;
		notas[2][1175] = g5;
		notas[2][1176] = g4;
		notas[2][1177] = g3;
		notas[2][1178] = g2;
		notas[2][1179] = g1;

		/** Linha[3] = Ré# */
		notas[3][73] = g1;
		notas[3][74] = g2;
		notas[3][75] = g3;
		notas[3][76] = g4;
		notas[3][77] = g5;
		notas[3][78] = g6;
		notas[3][79] = g5;
		notas[3][80] = g4;
		notas[3][81] = g3;
		notas[3][82] = g2;
		notas[3][83] = g1;

		notas[3][140] = g1;
		notas[3][141] = g2;
		notas[3][152] = g3;
		notas[3][153] = g4;
		notas[3][154] = g5;
		notas[3][155] = g6;
		notas[3][156] = g5;
		notas[3][157] = g4;
		notas[3][158] = g3;
		notas[3][159] = g2;
		notas[3][160] = g1;

		notas[3][306] = g1;
		notas[3][307] = g2;
		notas[3][308] = g3;
		notas[3][309] = g4;
		notas[3][310] = g5;
		notas[3][311] = g6;
		notas[3][312] = g5;
		notas[3][313] = g4;
		notas[3][314] = g3;
		notas[3][315] = g2;
		notas[3][316] = g1;

		notas[3][617] = g1;
		notas[3][618] = g2;
		notas[3][619] = g3;
		notas[3][620] = g4;
		notas[3][621] = g5;
		notas[3][622] = g6;
		notas[3][623] = g5;
		notas[3][624] = g4;
		notas[3][625] = g3;
		notas[3][626] = g2;
		notas[3][627] = g1;

		notas[3][1240] = g1;
		notas[3][1241] = g2;
		notas[3][1242] = g3;
		notas[3][1243] = g4;
		notas[3][1244] = g5;
		notas[3][1245] = g6;
		notas[3][1246] = g5;
		notas[3][1247] = g4;
		notas[3][1248] = g3;
		notas[3][1249] = g2;
		notas[3][1250] = g1;

		/** Linha[4] = Mi */
		notas[4][77] = g1;
		notas[4][78] = g2;
		notas[4][79] = g3;
		notas[4][80] = g4;
		notas[4][81] = g5;
		notas[4][82] = g6;
		notas[4][83] = g5;
		notas[4][84] = g4;
		notas[4][85] = g3;
		notas[4][86] = g2;
		notas[4][87] = g1;

		notas[4][160] = g1;
		notas[4][161] = g2;
		notas[4][162] = g3;
		notas[4][163] = g4;
		notas[4][164] = g5;
		notas[4][165] = g6;
		notas[4][166] = g5;
		notas[4][167] = g4;
		notas[4][168] = g3;
		notas[4][169] = g2;
		notas[4][170] = g1;

		notas[4][325] = g1;
		notas[4][326] = g2;
		notas[4][327] = g3;
		notas[4][328] = g4;
		notas[4][329] = g5;
		notas[4][330] = g6;
		notas[4][331] = g5;
		notas[4][332] = g4;
		notas[4][333] = g3;
		notas[4][334] = g2;
		notas[4][335] = g1;

		notas[4][655] = g1;
		notas[4][656] = g2;
		notas[4][657] = g3;
		notas[4][658] = g4;
		notas[4][659] = g5;
		notas[4][660] = g6;
		notas[4][661] = g5;
		notas[4][662] = g4;
		notas[4][663] = g3;
		notas[4][664] = g2;
		notas[4][665] = g1;

		notas[4][1313] = g1;
		notas[4][1314] = g2;
		notas[4][1315] = g3;
		notas[4][1316] = g4;
		notas[4][1317] = g5;
		notas[4][1318] = g6;
		notas[4][1319] = g5;
		notas[4][1320] = g4;
		notas[4][1321] = g3;
		notas[4][1322] = g2;
		notas[4][1323] = g1;

		/** Linha[5] = Fá */
		notas[5][82] = g1;
		notas[5][83] = g2;
		notas[5][84] = g3;
		notas[5][85] = g4;
		notas[5][86] = g5;
		notas[5][87] = g6;
		notas[5][88] = g5;
		notas[5][89] = g4;
		notas[5][90] = g3;
		notas[5][91] = g2;
		notas[5][92] = g1;

		notas[5][170] = g1;
		notas[5][171] = g2;
		notas[5][172] = g3;
		notas[5][173] = g4;
		notas[5][174] = g5;
		notas[5][175] = g6;
		notas[5][176] = g5;
		notas[5][177] = g4;
		notas[5][178] = g3;
		notas[5][189] = g2;
		notas[5][190] = g1;

		notas[5][344] = g1;
		notas[5][345] = g2;
		notas[5][346] = g3;
		notas[5][347] = g4;
		notas[5][348] = g5;
		notas[5][349] = g6;
		notas[5][350] = g5;
		notas[5][351] = g4;
		notas[5][352] = g3;
		notas[5][353] = g2;
		notas[5][354] = g1;

		notas[5][693] = g1;
		notas[5][694] = g2;
		notas[5][695] = g3;
		notas[5][696] = g4;
		notas[5][697] = g5;
		notas[5][698] = g6;
		notas[5][699] = g5;
		notas[5][700] = g4;
		notas[5][701] = g3;
		notas[5][702] = g2;
		notas[5][703] = g1;

		notas[5][1392] = g1;
		notas[5][1393] = g2;
		notas[5][1394] = g3;
		notas[5][1395] = g4;
		notas[5][1396] = g5;
		notas[5][1397] = g6;
		notas[5][1398] = g5;
		notas[5][1399] = g4;
		notas[5][1400] = g3;
		notas[5][1401] = g2;
		notas[5][1402] = g1;

		/** Linha[6] = Fá# */
		notas[6][88] = g1;
		notas[6][89] = g2;
		notas[6][90] = g3;
		notas[6][91] = g4;
		notas[6][92] = g5;
		notas[6][93] = g6;
		notas[6][94] = g5;
		notas[6][95] = g4;
		notas[6][96] = g3;
		notas[6][97] = g2;
		notas[6][98] = g1;

		notas[6][180] = g1;
		notas[6][181] = g2;
		notas[6][182] = g3;
		notas[6][183] = g4;
		notas[6][184] = g5;
		notas[6][185] = g6;
		notas[6][186] = g5;
		notas[6][187] = g4;
		notas[6][188] = g3;
		notas[6][189] = g2;
		notas[6][190] = g1;

		notas[6][365] = g1;
		notas[6][366] = g2;
		notas[6][367] = g3;
		notas[6][368] = g4;
		notas[6][369] = g5;
		notas[6][370] = g6;
		notas[6][371] = g5;
		notas[6][372] = g4;
		notas[6][373] = g3;
		notas[6][374] = g2;
		notas[6][375] = g1;

		notas[6][735] = g1;
		notas[6][736] = g2;
		notas[6][737] = g3;
		notas[6][738] = g4;
		notas[6][739] = g5;
		notas[6][740] = g6;
		notas[6][741] = g5;
		notas[6][742] = g4;
		notas[6][743] = g3;
		notas[6][744] = g2;
		notas[6][745] = g1;

		notas[6][1475] = g1;
		notas[6][1476] = g2;
		notas[6][1477] = g3;
		notas[6][1478] = g4;
		notas[6][1479] = g5;
		notas[6][1480] = g6;
		notas[6][1481] = g5;
		notas[6][1482] = g4;
		notas[6][1483] = g3;
		notas[6][1484] = g2;
		notas[6][1485] = g1;

		/** Linha[7] = Sol */
		notas[7][93] = g1;
		notas[7][94] = g2;
		notas[7][95] = g3;
		notas[7][96] = g4;
		notas[7][97] = g5;
		notas[7][98] = g6;
		notas[7][99] = g5;
		notas[7][100] = g4;
		notas[7][101] = g3;
		notas[7][102] = g2;
		notas[7][103] = g1;

		notas[7][191] = g1;
		notas[7][192] = g2;
		notas[7][193] = g3;
		notas[7][194] = g4;
		notas[7][195] = g5;
		notas[7][196] = g6;
		notas[7][197] = g5;
		notas[7][198] = g4;
		notas[7][199] = g3;
		notas[7][200] = g2;
		notas[7][201] = g1;

		notas[7][387] = g1;
		notas[7][388] = g2;
		notas[7][389] = g3;
		notas[7][390] = g4;
		notas[7][391] = g5;
		notas[7][392] = g6;
		notas[7][393] = g5;
		notas[7][394] = g4;
		notas[7][395] = g3;
		notas[7][396] = g2;
		notas[7][397] = g1;

		notas[7][779] = g1;
		notas[7][780] = g2;
		notas[7][781] = g3;
		notas[7][782] = g4;
		notas[7][783] = g5;
		notas[7][784] = g6;
		notas[7][785] = g5;
		notas[7][786] = g4;
		notas[7][787] = g3;
		notas[7][788] = g2;
		notas[7][789] = g1;

		notas[7][1563] = g1;
		notas[7][1564] = g2;
		notas[7][1565] = g3;
		notas[7][1566] = g4;
		notas[7][1567] = g5;
		notas[7][1568] = g6;
		notas[7][1569] = g5;
		notas[7][1570] = g4;
		notas[7][1571] = g3;
		notas[7][1572] = g2;
		notas[7][1573] = g1;

		/** Linha[8] = Sol# */
		notas[8][101] = g1;
		notas[8][100] = g2;
		notas[8][101] = g3;
		notas[8][102] = g4;
		notas[8][103] = g5;
		notas[8][104] = g6;
		notas[8][105] = g5;
		notas[8][106] = g4;
		notas[8][107] = g3;
		notas[8][108] = g2;
		notas[8][109] = g1;

		notas[8][203] = g1;
		notas[8][204] = g2;
		notas[8][205] = g3;
		notas[8][206] = g4;
		notas[8][207] = g5;
		notas[8][208] = g6;
		notas[8][209] = g5;
		notas[8][210] = g4;
		notas[8][211] = g3;
		notas[8][212] = g2;
		notas[8][213] = g1;

		notas[8][410] = g1;
		notas[8][411] = g2;
		notas[8][412] = g3;
		notas[8][413] = g4;
		notas[8][414] = g5;
		notas[8][415] = g6;
		notas[8][416] = g5;
		notas[8][417] = g4;
		notas[8][418] = g3;
		notas[8][419] = g2;
		notas[8][420] = g1;

		notas[8][825] = g1;
		notas[8][826] = g2;
		notas[8][827] = g3;
		notas[8][828] = g4;
		notas[8][829] = g5;
		notas[8][830] = g6;
		notas[8][831] = g5;
		notas[8][832] = g4;
		notas[8][833] = g3;
		notas[8][834] = g2;
		notas[8][835] = g1;

		notas[8][1656] = g1;
		notas[8][1657] = g2;
		notas[8][1658] = g3;
		notas[8][1659] = g4;
		notas[8][1660] = g5;
		notas[8][1661] = g6;
		notas[8][1662] = g5;
		notas[8][1663] = g4;
		notas[8][1664] = g3;
		notas[8][1665] = g2;
		notas[8][1666] = g1;

		/** Linha[9] = La */
		notas[9][105] = g1;
		notas[9][106] = g2;
		notas[9][107] = g3;
		notas[9][108] = g4;
		notas[9][109] = g5;
		notas[9][110] = g6;
		notas[9][111] = g5;
		notas[9][112] = g4;
		notas[9][113] = g3;
		notas[9][114] = g2;
		notas[9][115] = g1;

		notas[9][215] = g1;
		notas[9][216] = g2;
		notas[9][217] = g3;
		notas[9][218] = g4;
		notas[9][219] = g5;
		notas[9][220] = g6;
		notas[9][221] = g5;
		notas[9][222] = g4;
		notas[9][223] = g3;
		notas[9][224] = g2;
		notas[9][225] = g1;

		notas[9][435] = g1;
		notas[9][436] = g2;
		notas[9][437] = g3;
		notas[9][438] = g4;
		notas[9][439] = g5;
		notas[9][440] = g6;
		notas[9][441] = g5;
		notas[9][442] = g4;
		notas[9][443] = g3;
		notas[9][444] = g2;
		notas[9][445] = g1;

		notas[9][875] = g1;
		notas[9][876] = g2;
		notas[9][877] = g3;
		notas[9][878] = g4;
		notas[9][879] = g5;
		notas[9][880] = g6;
		notas[9][881] = g5;
		notas[9][882] = g4;
		notas[9][883] = g3;
		notas[9][884] = g2;
		notas[9][885] = g1;

		notas[9][1755] = g1;
		notas[9][1756] = g2;
		notas[9][1757] = g3;
		notas[9][1758] = g4;
		notas[9][1759] = g5;
		notas[9][1760] = g6;
		notas[9][1761] = g5;
		notas[9][1762] = g4;
		notas[9][1763] = g3;
		notas[9][1764] = g2;
		notas[9][1765] = g1;

		/** Linha[10] = La# */
		notas[10][111] = g1;
		notas[10][112] = g2;
		notas[10][113] = g3;
		notas[10][114] = g4;
		notas[10][115] = g5;
		notas[10][116] = g6;
		notas[10][117] = g5;
		notas[10][118] = g4;
		notas[10][119] = g3;
		notas[10][120] = g2;
		notas[10][121] = g1;

		notas[10][228] = g1;
		notas[10][229] = g2;
		notas[10][230] = g3;
		notas[10][231] = g4;
		notas[10][232] = g5;
		notas[10][233] = g6;
		notas[10][234] = g5;
		notas[10][235] = g4;
		notas[10][236] = g3;
		notas[10][237] = g2;
		notas[10][238] = g1;

		notas[10][461] = g1;
		notas[10][462] = g2;
		notas[10][463] = g3;
		notas[10][464] = g4;
		notas[10][465] = g5;
		notas[10][466] = g6;
		notas[10][467] = g5;
		notas[10][468] = g4;
		notas[10][469] = g3;
		notas[10][470] = g2;
		notas[10][471] = g1;

		notas[10][927] = g1;
		notas[10][928] = g2;
		notas[10][929] = g3;
		notas[10][930] = g4;
		notas[10][931] = g5;
		notas[10][932] = g6;
		notas[10][933] = g5;
		notas[10][934] = g4;
		notas[10][935] = g3;
		notas[10][936] = g2;
		notas[10][937] = g1;

		notas[10][1859] = g1;
		notas[10][1860] = g2;
		notas[10][1861] = g3;
		notas[10][1862] = g4;
		notas[10][1863] = g5;
		notas[10][1864] = g6;
		notas[10][1865] = g5;
		notas[10][1866] = g4;
		notas[10][1867] = g3;
		notas[10][1868] = g2;
		notas[10][1869] = g1;

		/** Linha[11] = Si */
		notas[11][119] = g1;
		notas[11][120] = g2;
		notas[11][121] = g3;
		notas[11][122] = g4;
		notas[11][123] = g5;
		notas[11][124] = g6;
		notas[11][125] = g5;
		notas[11][126] = g4;
		notas[11][127] = g3;
		notas[11][128] = g2;
		notas[11][129] = g1;

		notas[11][242] = g1;
		notas[11][243] = g2;
		notas[11][244] = g3;
		notas[11][245] = g4;
		notas[11][246] = g5;
		notas[11][247] = g6;
		notas[11][248] = g5;
		notas[11][249] = g4;
		notas[11][250] = g3;
		notas[11][251] = g2;
		notas[11][252] = g1;

		notas[11][489] = g1;
		notas[11][490] = g2;
		notas[11][491] = g3;
		notas[11][492] = g4;
		notas[11][493] = g5;
		notas[11][494] = g6;
		notas[11][495] = g5;
		notas[11][496] = g4;
		notas[11][497] = g3;
		notas[11][498] = g2;
		notas[11][499] = g1;

		notas[11][983] = g1;
		notas[11][984] = g2;
		notas[11][985] = g3;
		notas[11][986] = g4;
		notas[11][987] = g5;
		notas[11][988] = g6;
		notas[11][989] = g5;
		notas[11][990] = g4;
		notas[11][991] = g3;
		notas[11][992] = g2;
		notas[11][993] = g1;

		notas[11][1971] = g1;
		notas[11][1972] = g2;
		notas[11][1973] = g3;
		notas[11][1974] = g4;
		notas[11][1975] = g5;
		notas[11][1976] = g6;
		notas[11][1977] = g5;
		notas[11][1978] = g4;
		notas[11][1979] = g3;
		notas[11][1980] = g2;
		notas[11][1981] = g1;

		/**
		 * BD de acordes
		 */
		// Inicializando com zeros a matriz de acordes
		for (int linha = 0; linha < 48; linha++) {
			for (int coluna = 0; coluna < 12; coluna++) {
				acordes[linha][coluna] = 0;
			}
		}

		// Escrevendo os acordes certos
		// Acordes de C
		/* CM */
		acordes[0][0] = 1;
		acordes[0][4] = 1;
		acordes[0][7] = 1;
		/* Cm */
		acordes[1][0] = 1;
		acordes[1][3] = 1;
		acordes[1][7] = 1;
		/* Caum */
		acordes[2][0] = 1;
		acordes[2][4] = 1;
		acordes[2][8] = 1;
		/* Cdim */
		acordes[3][0] = 1;
		acordes[3][3] = 1;
		acordes[3][6] = 1;
		// Acordes de C#
		/* C#M */
		acordes[4][1] = 1;
		acordes[4][5] = 1;
		acordes[4][8] = 1;
		/* C#m */
		acordes[5][1] = 1;
		acordes[5][4] = 1;
		acordes[5][8] = 1;
		/* C#aum */
		acordes[6][1] = 1;
		acordes[6][5] = 1;
		acordes[6][9] = 1;
		/* C#dim */
		acordes[7][1] = 1;
		acordes[7][4] = 1;
		acordes[7][7] = 1;
		// Acordes de D
		/* DM */
		acordes[8][2] = 1;
		acordes[8][6] = 1;
		acordes[8][9] = 1;
		/* Dm */
		acordes[9][2] = 1;
		acordes[9][5] = 1;
		acordes[9][9] = 1;
		/* Daum */
		acordes[10][2] = 1;
		acordes[10][6] = 1;
		acordes[10][10] = 1;
		/* Ddim */
		acordes[11][2] = 1;
		acordes[11][5] = 1;
		acordes[11][8] = 1;
		// Acordes de D#
		/* D#M */
		acordes[12][3] = 1;
		acordes[12][7] = 1;
		acordes[12][10] = 1;
		/* D#m */
		acordes[13][3] = 1;
		acordes[13][6] = 1;
		acordes[13][10] = 1;
		/* D#aum */
		acordes[14][3] = 1;
		acordes[14][7] = 1;
		acordes[14][11] = 1;
		/* D#dim */
		acordes[15][3] = 1;
		acordes[15][6] = 1;
		acordes[15][9] = 1;
		// Acordes de E
		/* EM */
		acordes[16][4] = 1;
		acordes[16][8] = 1;
		acordes[16][11] = 1;
		/* Em */
		acordes[17][4] = 1;
		acordes[17][7] = 1;
		acordes[17][11] = 1;
		/* Eaum */
		acordes[18][4] = 1;
		acordes[18][8] = 1;
		acordes[18][0] = 1;
		/* Edim */
		acordes[19][4] = 1;
		acordes[19][7] = 1;
		acordes[19][10] = 1;
		// Acordes de F
		/* FM */
		acordes[20][5] = 1;
		acordes[20][9] = 1;
		acordes[20][0] = 1;
		/* Fm */
		acordes[21][5] = 1;
		acordes[21][8] = 1;
		acordes[21][0] = 1;
		/* Faum */
		acordes[22][5] = 1;
		acordes[22][9] = 1;
		acordes[22][1] = 1;
		/* Fdim */
		acordes[23][5] = 1;
		acordes[23][8] = 1;
		acordes[23][11] = 1;
		// Acordes de F#
		/* F#M */
		acordes[24][6] = 1;
		acordes[24][10] = 1;
		acordes[24][1] = 1;
		/* F#m */
		acordes[25][6] = 1;
		acordes[25][9] = 1;
		acordes[25][1] = 1;
		/* F#aum */
		acordes[26][6] = 1;
		acordes[26][10] = 1;
		acordes[26][2] = 1;
		/* F#dim */
		acordes[27][6] = 1;
		acordes[27][9] = 1;
		acordes[27][0] = 1;
		// Acordes de G
		/* GM */
		acordes[28][7] = 1;
		acordes[28][11] = 1;
		acordes[28][2] = 1;
		/* Gm */
		acordes[29][7] = 1;
		acordes[29][10] = 1;
		acordes[29][2] = 1;
		/* Gaum */
		acordes[30][7] = 1;
		acordes[30][11] = 1;
		acordes[30][3] = 1;
		/* Gdim */
		acordes[31][7] = 1;
		acordes[31][10] = 1;
		acordes[31][1] = 1;
		// Acordes de G#
		/* G#M */
		acordes[32][8] = 1;
		acordes[32][0] = 1;
		acordes[32][3] = 1;
		/* G#m */
		acordes[33][8] = 1;
		acordes[33][11] = 1;
		acordes[33][3] = 1;
		/* G#aum */
		acordes[34][8] = 1;
		acordes[34][0] = 1;
		acordes[34][4] = 1;
		/* G#dim */
		acordes[35][8] = 1;
		acordes[35][11] = 1;
		acordes[35][2] = 1;
		// Acordes de A
		/* AM */
		acordes[36][9] = 1;
		acordes[36][1] = 1;
		acordes[36][4] = 1;
		/* Am */
		acordes[37][9] = 1;
		acordes[37][0] = 1;
		acordes[37][4] = 1;
		/* Aaum */
		acordes[38][9] = 1;
		acordes[38][1] = 1;
		acordes[38][5] = 1;
		/* Adim */
		acordes[39][9] = 1;
		acordes[39][0] = 1;
		acordes[39][3] = 1;
		// Acordes A#
		/* A#M */
		acordes[40][10] = 1;
		acordes[40][2] = 1;
		acordes[40][5] = 1;
		/* A#m */
		acordes[41][10] = 1;
		acordes[41][1] = 1;
		acordes[41][5] = 1;
		/* A#aum */
		acordes[42][10] = 1;
		acordes[42][2] = 1;
		acordes[42][6] = 1;
		/* A#dim */
		acordes[43][10] = 1;
		acordes[43][1] = 1;
		acordes[43][4] = 1;
		// Acordes B
		/* BM */
		acordes[44][11] = 1;
		acordes[44][3] = 1;
		acordes[44][6] = 1;
		/* Bm */
		acordes[45][11] = 1;
		acordes[45][2] = 1;
		acordes[45][6] = 1;
		/* Baum */
		acordes[46][11] = 1;
		acordes[46][3] = 1;
		acordes[46][7] = 1;
		/* Bdim */
		acordes[47][11] = 1;
		acordes[47][2] = 1;
		acordes[47][5] = 1;

	}

	public void setByteArraySong(byte[] songInByte) {

		this.songInByte = songInByte;

	}

	public void setS1(float[] S1) {
		this.S1 = S1;
	}

	public float[] getsomFFT() {
		return respFreq;
	}

	public float[] getFreq() {
		return f;
	}

	public float getEnergy() {
		return energy;
	}

	public float[] getS1() {
		// Transformando o vetor de bytes para vetor de floats
		float maxsongInFloat = 0;
		this.songInFloat = new float[songInByte.length / 2];
		for (int i = 0; i < songInFloat.length; i++) {
			songInFloat[i] = ((songInByte[i * 2] & 0XFF) | (songInByte[i * 2 + 1] << 8)) / 32768.0F;
			if (songInFloat[i] > maxsongInFloat) {
				maxsongInFloat = songInFloat[i];
			}
		}

		// Equalizando o som e calculando energia total
		float sum = 0;
		for (int i = 0; i < songInFloat.length; i++) {
			songInFloat[i] = songInFloat[i] / maxsongInFloat;
			sum += (songInFloat[i] * songInFloat[i]);
		}
		this.energy = (sum / songInFloat.length);

		System.out.println("Energia total: " + this.energy);

		this.Som = songInFloat;

		// Preparando para a transformada de fourier
		this.n = (int) Math.pow(2, (int) (Math.log(Som.length) / Math.log(2)));
		this.m = (int) (Math.log(Som.length) / Math.log(2));

		cos = new float[n / 2];
		sin = new float[n / 2];

		for (int i = 0; i < n / 2; i++) {
			cos[i] = (float) Math.cos(-2 * Math.PI * i / n);
			sin[i] = (float) Math.sin(-2 * Math.PI * i / n);
		}

		// FFT in real case
		int i, j, k, n1, n2, a;
		float c, s, t1, t2;
		float[] x, y;
		x = Som;
		y = new float[x.length];
		// Zerando a parte imaginária do som
		for (int l = 0; l < n; l++) {
			y[l] = 0;
		}
		// Bit-reverse
		j = 0;
		n2 = n / 2;
		for (i = 1; i < n - 1; i++) {
			n1 = n2;
			while (j >= n1) {
				j = j - n1;
				n1 = n1 / 2;
			}
			j = j + n1;
			if (i < j) {
				t1 = x[i];
				x[i] = x[j];
				x[j] = t1;
				t1 = y[i];
				y[i] = y[j];
				y[j] = t1;
			}
		}
		// FFT
		n1 = 0;
		n2 = 1;
		for (i = 0; i < m; i++) {
			n1 = n2;
			n2 = n2 + n2;
			a = 0;
			for (j = 0; j < n1; j++) {
				c = cos[a];
				s = sin[a];
				a += 1 << (m - i - 1);
				for (k = j; k < n; k = k + n2) {
					t1 = c * x[k + n1] - s * y[k + n1];
					t2 = s * x[k + n1] + c * y[k + n1];
					x[k + n1] = x[k] - t1;
					y[k + n1] = y[k] - t2;
					x[k] = x[k] + t1;
					y[k] = y[k] + t2;
				}
			}
		}

		// ABS(FFT)
		float MaxFFTSom = 0;
		float[] SomFFT = new float[x.length];
		for (int l = 0; l < x.length; l++) {
			SomFFT[l] = (float) Math.sqrt(x[l] * x[l] + y[l] * y[l]);
			if (SomFFT[l] > MaxFFTSom) {
				MaxFFTSom = SomFFT[l];
			}
		}

		// Equalizando o FFT
		somFFT = new float[Math.round((SomFFT.length) / 2)];
		f = new float[somFFT.length];
		for (int l = 0; l < Math.round((SomFFT.length) / 2); l++) {
			somFFT[l] = SomFFT[l] / MaxFFTSom;
			// Ajustando vetor frequencia
			f[l] = l * fs / n;
		}

		/**
		 * Fazendo o famoso acoplamento Transformando as respostas num vetor que
		 * cada slot significa a frequencia tipo (261,1) = Um Dó se não me
		 * engano
		 */
		int ll = 0;
		int jl = 0;
		int il = 0;
		float SOMA = 0;
		respfreq = new float[Math.round(f[f.length - 1])];
		while (il < f.length - 1) {
			if (Math.round(f[il]) == Math.round(f[il + 1])) {
				SOMA = somFFT[il + 1] + SOMA;
				jl++;
			} else {
				respfreq[ll] = SOMA / (jl + 1);
				jl = 0;
				SOMA = somFFT[il + 1];
				ll++;
			}
			il++;
		}
		ll = 0;
		jl = 0;
		il = 0;

		/**
		 * Equalizando o vetor respfreq / criando o vetor correto
		 */
		respFreq = new float[1986];
		float maxRespFreq = 0;
		for (int l = 0; l < respfreq.length; l++) {
			if (maxRespFreq < respfreq[l]) {
				maxRespFreq = respfreq[l];
			}
		}
		for (int l = 0; l < 1985; l++) {
			respFreq[l + 1] = respfreq[l] / maxRespFreq;
		}

		/**
		 * RADIAL BASIS LAYER para BD notas
		 */
		float[] nota = new float[1986];
		for (int l = 0; l < 12; l++) {

			// escolhendo a nota
			for (int l2 = 0; l2 < nota.length; l2++) {
				nota[l2] = notas[l][l2];
			}

			// Calcular médiaX e mediaY
			float somaX = 0;
			float somaY = 0;
			for (int l3 = 0; l3 < respFreq.length; l3++) {
				somaX = somaX + respFreq[l3];
				somaY = somaY + nota[l3];
			}
			float mediaX = somaX / respFreq.length;
			float mediaY = somaY / nota.length;

			// Calcular varX, varY e covXY
			float varX = 0;
			float varY = 0;
			float covXY = 0;
			for (int l4 = 0; l4 < respFreq.length; l4++) {
				varX = (float) (respFreq[l4] - mediaX)
						* (respFreq[l4] - mediaX) + varX;
				varY = (float) (nota[l4] - mediaY) * (nota[l4] - mediaY) + varY;
				covXY = (respFreq[l4] - mediaX) * (nota[l4] - mediaY) + covXY;
			}

			// Calcular correlação
			S1[l] = (float) Math.sqrt(Math.pow(
					(covXY / Math.sqrt(varX * varY)), 2));
			System.out.println("S1:" + S1[l]);
		}

		// Normalizacao
		float min = 1;
		for (int m = 0; m < S1.length; m++) {
			if (S1[m] < min) {
				min = S1[m];
			}
		}

		for (int l = 0; l < S1.length; l++) {
			S1[l] = (S1[l] - min);
		}

		float max = 0;
		for (int l = 0; l < S1.length; l++) {
			if (S1[l] > max) {
				max = S1[l];
			}
		}

		for (int l = 0; l < S1.length; l++) {
			S1[l] = S1[l] / max;
		}

		return S1;
	}

	public float[] getS2() {
		return S2;
	}

	public int getAcorde() {

		/**
		 * RADIAL BASIS LAYER para BD Acordes
		 */
		float[] acorde = new float[12];
		for (int l = 0; l < 48; l++) {

			// escolhendo o acorde
			for (int l2 = 0; l2 < acorde.length; l2++) {
				acorde[l2] = acordes[l][l2];
			}

			// Calcular médiaX e mediaY
			float somaX = 0;
			float somaY = 0;
			for (int l3 = 0; l3 < S1.length; l3++) {
				somaX = somaX + S1[l3];
				somaY = somaY + acorde[l3];
			}
			float mediaX = somaX / S1.length;
			float mediaY = somaY / acorde.length;

			// Calcular varX, varY e covXY
			float varX = 0;
			float varY = 0;
			float covXY = 0;
			for (int l4 = 0; l4 < S1.length; l4++) {
				varX = (float) (S1[l4] - mediaX) * (S1[l4] - mediaX) + varX;
				varY = (float) (acorde[l4] - mediaY) * (acorde[l4] - mediaY)
						+ varY;
				covXY = (S1[l4] - mediaX) * (acorde[l4] - mediaY) + covXY;
			}

			// Calcular correlação
			S2[l] = (float) Math.sqrt(Math.pow(
					(covXY / Math.sqrt(varX * varY)), 2));
		}

		/**
		 * Decodificando o acorde
		 */
		// Pegando o máximo de S2
		float maxS2 = 0;
		for (int l = 0; l < S2.length; l++) {
			if (S2[l] > maxS2) {
				maxS2 = S2[l];
			}
		}

		// Pegando o slot do valor máximo de S2
		int slotMaxS2 = 0;
		for (int l = 0; l < S2.length; l++) {
			if (S2[l] == maxS2) {
				slotMaxS2 = l;
			}
		}

		// Procurando no vetor Acordes o acorde tocado
		Acorde = nomeAcordes[slotMaxS2];

		return slotMaxS2;
	}

}
