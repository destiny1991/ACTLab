package buaa.data;
import java.util.*;

public class Instruction {
	private String name = null;
	private Map paras = null;
	private Map semantic = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map getParas() {
		return paras;
	}
	public void setParas(Map paras) {
		this.paras = paras;
	}
	public Map getSemantic() {
		return semantic;
	}
	public void setSemantic(Map semantic) {
		this.semantic = semantic;
	}
	
	
	public String toString() {
		return name + ":" + paras + "\n+" + semantic;
	}  
	
	public Map analyzeParas(List<String> strList) {
		Map map = new HashMap();
		switch(strList.get(0)) {
			case "and":
			case "and.":
			case "andc":
			case "andc.":
			case "nand":
			case "nand.":
			case "nor":
			case "nor.":
			case "or":
			case "or.":
			case "orc":
			case "orc.":
			case "xor":
			case "xor.":
			case "eqv":
			case "eqv.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("rB", strList.get(3));
				break;
				
			case "andi.":
			case "andis.":
			case "ori":
			case "oris":
			case "xori":
			case "xoris":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("UIMM", strList.get(3));
				break;
			
			case "add":
			case "add.":
			case "addo":
			case "addo.":
			case "addc":
			case "addc.":
			case "addco":
			case "addco.":
			case "adde":
			case "adde.":
			case "addeo":
			case "addeo.":
			case "subf":
			case "subf.":
			case "subfo":
			case "subfo.":
			case "subfc":
			case "subfc.":
			case "subfco":
			case "subfco.":
			case "subfe":
			case "subfe.":
			case "subfeo":
			case "subfeo.":
			
			case "divw":
			case "divw.":
			case "divwo":
			case "divwo.":
			case "divwu":
			case "divwu.":
			case "divwuo":
			case "divwuo.":
			case "mulhw":
			case "mulhw.":
			case "mulhwu":
			case "mulhwu.":
			case "mullw":
			case "mullw.":
			case "mullwo":
			case "mullwo.":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
				
			case "addi":
			case "addic":
			case "addic.":
			case "addis":
			case "subfic":
			
			case "mulli":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("SIMM", strList.get(3));
				break;
			
			case "addme":
			case "addme.":
			case "addmeo":
			case "addmeo.":
			case "addze":
			case "addze.":
			case "addzeo":
			case "addzeo.":
			case "neg":
			case "neg.":
			case "nego":
			case "nego.":
			case "subfme":
			case "subfme.":
			case "subfmeo":
			case "subfmeo.":
			case "subfze":
			case "subfze.":
			case "subfzeo":
			case "subfzeo.":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				break;
			
			case "b":
			case "ba":
			case "bl":
			case "bla":
				map.put("LI", strList.get(1));
				break;
			
			case "bc":
			case "bca":
			case "bcl":
			case "bcla":
				map.put("BO", strList.get(1));
				map.put("BI", strList.get(2));
				map.put("BD", strList.get(3));
				break;
			
			case "bcctr":
			case "bcctrl":
			case "bclr":
			case "bclrl":
				map.put("BO", strList.get(1));
				map.put("BI", strList.get(2));
				break;
			
			case "cmp":
			case "cmpl":
				map.put("crfD", strList.get(1));
				map.put("L", strList.get(2));
				map.put("rA", strList.get(3));
				map.put("rB", strList.get(4));
				break;
			case "cmpi":
				map.put("crfD", strList.get(1));
				map.put("L", strList.get(2));
				map.put("rA", strList.get(3));
				map.put("SIMM", strList.get(4));
				break;
			case "cmpli":
				map.put("crfD", strList.get(1));
				map.put("L", strList.get(2));
				map.put("rA", strList.get(3));
				map.put("UIMM", strList.get(4));
				break;
				
			case "slw":
			case "slw.":
			case "sraw":
			case "sraw.":
			case "srw":
			case "srw.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "srawi":
			case "srawi.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("SH", strList.get(3));
				break;
			case "rlwimi":
			case "rlwimi.":
			case "rlwinm":
			case "rlwinm.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("SH", strList.get(3));
				map.put("MB", strList.get(4));
				map.put("ME", strList.get(5));
				break;
				
			case "rlwnm":
			case "rlwnm.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				map.put("rB", strList.get(3));
				map.put("MB", strList.get(4));
				map.put("ME", strList.get(5));
				break;
				
			case "lbz":
			case "lbzu":
			case "lha":
			case "lhau":
			case "lhz":
			case "lhzu":
			case "lwz":
			case "lwzu":
				map.put("rD", strList.get(1));
				map.put("d", strList.get(2));
				map.put("rA", strList.get(3));
				break;
			
			case "lmw":
				map.put("rD", strList.get(1));
				map.put("D", strList.get(2));
				map.put("rA", strList.get(3));
				break;
				
			case "lbzux":
			case "lbzx":
			case "lhaux":
			case "lhax":
			case "lhbrx":
			case "lhzux":
			case "lhzx":
			case "lwbrx":
			case "lwzux":
			case "lwzx":
			case "lbdx":
			case "lhdx":
			case "lwdx":
			case "lwarx":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "dsn":
				map.put("rA", strList.get(1));
				map.put("rB", strList.get(2));
				break;
			case "cntlzw":
			case "cntlzw.":
				map.put("rA", strList.get(1));
				map.put("rS", strList.get(2));
				break;
			
			case "stb":
			case "stbu":
			case "sth":
			case "sthu":
			case "stw":
			case "stwu":
				map.put("rS", strList.get(1));
				map.put("d", strList.get(2));
				map.put("rA", strList.get(3));
				break;
			
			case "stmw":
				map.put("rS", strList.get(1));
				map.put("D", strList.get(2));
				map.put("rA", strList.get(3));
				break;
				
			case "stbux":
			case "stbx":
			case "sthbrx":
			case "sthux":
			case "sthx":
			case "stwbrx":
			case "stwux":
			case "stwx":
			case "stbdx":
			case "sthdx":
			case "stwdx":
			case "stwcx.":
				map.put("rS", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "stfddx":
				map.put("frS", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			
			case "tw":
				map.put("TO", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "twi":
				map.put("TO", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("SIMM", strList.get(3));
				break;
				
			case "crand":
			case "crandc":
			case "creqv":
			case "crnand":
			case "crnor":
			case "cror":
			case "crxor":
				map.put("crbD", strList.get(1));
				map.put("crbA", strList.get(2));
				map.put("crbB", strList.get(3));
				break;
			
			case "mcrf":
				map.put("crfD", strList.get(1));
				map.put("crfS", strList.get(2));
				break;
			case "mcrxr":
				map.put("crfD", strList.get(1));
				break;
			case "mfcr":
			case "mfmsr":
				map.put("rD", strList.get(1));
				break;
			case "mfspr":
				map.put("rD", strList.get(1));
				map.put("SPRN", strList.get(2));
				break;
			case "mftb":
				map.put("rD", strList.get(1));
				map.put("TBRN", strList.get(2));
				break;
			case "mtcrf":
				map.put("CRM", strList.get(1));
				map.put("rS", strList.get(2));
				break;
			case "mtspr":
				map.put("SPRN", strList.get(1));
				map.put("rS", strList.get(2));
				break;
			case "mtmsr":	
				map.put("rS", strList.get(1));
				break;
			case "mfocrf":	
				map.put("rD", strList.get(1));
				map.put("FXM", strList.get(2));
				break;
			case "isel":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				map.put("crb", strList.get(4));
				break;
			case "wrtee":
				map.put("rS", strList.get(1));
				break;
			case "wrteei":
				map.put("E", strList.get(1));
				break;
			case "msgclr":
			case "msgsnd":
				map.put("rB", strList.get(1));
				break;
			case "ehpriv":
				map.put("OC", strList.get(1));
				break;
			case "mbar":
				map.put("MO", strList.get(1));
				break;
				
			case "sc":
			case "isync":
			case "rfdi":
			case "rfgi":
			case "rfi":
			case "rfmci":
				break;
			case "sync":
				map.put("L", strList.get(1));
				map.put("E", strList.get(2));
				break;
			case "wait":
				map.put("WC", strList.get(1));
				map.put("WH", strList.get(2));
				break;
			
			case "dcba":	
			case "dcbal":
			case "dcbf":
			case "dcbi":
			case "dcbst":
			case "dcbz":
			case "dcbzep":
			case "dcbzl":
			case "dcbzlep":
			case "icbi":
				map.put("rA", strList.get(1));
				map.put("rB", strList.get(2));
				break;
			case "dcblc":
			case "dcbt":
			case "dcbtls":
			case "dcbtstls":
			case "icbt":
			case "icblc":
			case "icbtls":
				map.put("CT", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
				
			case "dcbfep":
			case "dcbstep":
			case "icbiep":
			case "tlbivax":
			case "tlbsx":
				map.put("rA", strList.get(1));
				map.put("rB", strList.get(2));
				break;
			case "dcbtep":
			case "dcbtstep":
				map.put("TH", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "lbepx":
			case "lhepx":
			case "lwepx":
				map.put("rD", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "tlbre":
				break;
			case "stbepx":
			case "sthepx":
			case "stwepx":
				map.put("rS", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "stfdepx":
				map.put("frS", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "tlbilx":	
				map.put("T", strList.get(1));
				map.put("rA", strList.get(2));
				map.put("rB", strList.get(3));
				break;
			case "tlbsync":
			case "tlbwe":
				break;
			
			case "dnh":
				map.put("DUI", strList.get(1));
				map.put("DCTL", strList.get(2));
				break;
			default:
				System.out.println("�޷�����" + strList.get(0) + "ָ�");
				break;
		}
		
		return map;
	}

	//��������ı�־λ
	public Map addMarkBit(String name, Map map) {
		switch(name) {
			case "and":
			case "andc":
			case "nand":
			case "nor":
			case "or":
			case "orc":
			case "xor":
			case "eqv":
			case "addic":
			case "mulhw":
			case "mulhwu":
			case "slw":
			case "sraw":
			case "srawi":
			case "srw":
			case "rlwimi":
			case "rlwinm":
			case "rlwnm":
				map.put("Rc", "0");
				break;
			case "and.":
			case "andc.":
			case "nand.":
			case "nor.":
			case "or.":
			case "orc.":
			case "xor.":
			case "eqv.":
			case "addic.":
			case "mulhw.":
			case "mulhwu.":
			case "slw.":
			case "sraw.":
			case "srawi.":
			case "srw.":
			case "rlwimi.":
			case "rlwinm.":
			case "rlwnm.":
				map.put("Rc", "1");
				break;
			
			case "add":
			case "addc":
			case "adde":
			case "addme":
			case "addze":
			case "neg":
			case "subf":
			case "subfc":
			case "subfe":
			case "subfme":
			case "subfze":
			case "divw":
			case "divwu":
			case "mullw":
				map.put("OE", "0");
				map.put("Rc", "0");
				break;
			case "add.":
			case "addc.":
			case "adde.":
			case "addme.":
			case "addze.":
			case "neg.":
			case "subf.":
			case "subfc.":
			case "subfe.":
			case "subfme.":
			case "subfze.":
			case "divw.":
			case "divwu.":
			case "mullw.":
				map.put("OE", "0");
				map.put("Rc", "1");
				break;
			case "addo":
			case "addco":
			case "addeo":
			case "addmeo":
			case "addzeo":
			case "nego":
			case "subfo":
			case "subfco":
			case "subfeo":
			case "subfmeo":
			case "subfzeo":
			case "divwo":
			case "divwuo":
			case "mullwo":
				map.put("OE", "1");
				map.put("Rc", "0");
				break;
			case "addo.":
			case "addco.":
			case "addeo.":
			case "addmeo.":
			case "addzeo.":
			case "nego.":
			case "subfo.":
			case "subfco.":
			case "subfeo.":
			case "subfmeo.":
			case "subfzeo.":
			case "divwo.":
			case "divwuo.":
			case "mullwo.":
				map.put("OE", "1");
				map.put("Rc", "1");
				break;
				
			case "b":
			case "bc":
				map.put("AA", "0");
				map.put("LK", "0");
				break;
			case "ba":
			case "bca":
				map.put("AA", "1");
				map.put("LK", "0");
				break;
			case "bl":
			case "bcl":
				map.put("AA", "0");
				map.put("LK", "1");
				break;
			case "bla":
			case "bcla":
				map.put("AA", "1");
				map.put("LK", "1");
				break;
				
			case "bcctr":
			case "bclr":
				map.put("LK", "0");
				break;
			case "bcctrl":
			case "bclrl":
				map.put("LK", "1");
				break;
				
			default: 
				break;
		}
		return map;
	}
	
	
	/**
	 * ���ܣ�����ָ������
	 * ����ɣ�2.1 �� 2.15
	 * ���ڣ�2015/04/22
	 */
	public Map addSemantic(String name, Map paras) {
		Map map = new HashMap();
		switch(name) {
			
			//2.1 �߼�����
			case "and":
			case "and.":
				map.put("GPR[" + paras.get("rS") + "]", 
						"GPR[" + paras.get("rA") + "]&GPR[" + paras.get("rB") + "]");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]&GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]&GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]&GPR[" + paras.get("rB") + "] = 0, " 
							+ "XER.SO}");
				}
				break;
				
			case "andc":
			case "andc.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]&~GPR[" + paras.get("rB") + "]");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rS") + "]&~GPR[" + paras.get("rB") + "] < 0, "
					        + "GPR[" + paras.get("rS") + "]&~GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rS") + "]&~GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "andi.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]&{16��b0, UIMM}");
				map.put("CR[0]", 
						"{GPR[" + paras.get("rS") + "]&{16��b0, UIMM} < 0, "
						+ "GPR[" + paras.get("rS") + "]&{16��b0, UIMM} > 0, " 
						+ "GPR[" + paras.get("rS") + "]&{16��b0, UIMM} = 0, " 
						+ "XER.SO}");
				break;
				
			case "andis.":	
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]&{UIMM, 16��b0}");
				map.put("CR[0]", 
						"{GPR[" + paras.get("rS") + "]&{UIMM, 16��b0} < 0, "
						+ "GPR[" + paras.get("rS") + "]&{UIMM, 16��b0} > 0, "
						+ "GPR[" + paras.get("rS") + "]&{UIMM, 16��b0} = 0, " 
						+ "XER.SO}");
			
			case "nand":
			case "nand.":
				map.put("GPR[" + paras.get("rA") + "]",
						"~(GPR[" + paras.get("rS") + "]&GPR[" + paras.get("rB") + "])");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{~(GPR[" + paras.get("rS") + "]&GPR[" + paras.get("rB") + "]) < 0, "
							+ "~(GPR[" + paras.get("rS") + "]&GPR[" + paras.get("rB") + "]) > 0, " 
							+ "~(GPR[" + paras.get("rS") + "]&GPR[" + paras.get("rB") + "]) = 0, " 
							+ "XER.SO}");
				}
				
			case "nor":
			case "nor.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"~(GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "])");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{~(GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "]) < 0, "
							+ "~(GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "]) > 0, "
							+ "~(GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "]) = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "or":
			case "or.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "]");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "] > 0, " 
							+ "GPR[" + paras.get("rS") + "]|GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "orc":
			case "orc.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]|~GPR[" + paras.get("rB") + "]");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rS") + "]|~GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rS") + "]|~GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rS") + "]|~GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "ori":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]|{16��b0,UIMM}");
				map.put("CR[0]",
						"{GPR[" + paras.get("rS") + "]|{16��b0,UIMM} < 0, "
						+ "GPR[" + paras.get("rS") + "]|{16��b0,UIMM} > 0, "
						+ "GPR[" + paras.get("rS") + "]|{16��b0,UIMM} = 0, "
						+ "XER.SO}");
				break;
			
			case "oris":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]|{UIMM, 16��b0}");
				map.put("CR[0]",
						"{GPR[" + paras.get("rS") + "]|{UIMM, 16��b0} < 0, "
						+ "GPR[" + paras.get("rS") + "]|{UIMM, 16��b0} > 0, "
						+ "GPR[" + paras.get("rS") + "]|{UIMM, 16��b0} = 0, "
						+ "XER.SO}");
				break;
			
			case "xor":
			case "xor.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "]"); //Java �е�������Ϊ"^"
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "xori":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]^{16��b0, UIMM}");
				map.put("CR[0]", 
						"{GPR[" + paras.get("rS") + "]^{16��b0, UIMM} < 0, "
						+ "GPR[" + paras.get("rS") + "]^{16��b0, UIMM} > 0, "
						+ "GPR[" + paras.get("rS") + "]^{16��b0, UIMM} = 0, "
						+ "XER.SO}");
				break;
				
			case "xoris":
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rS") + "]^{UIMM, 16��b0}");
				map.put("CR[0]", 
						"{GPR[" + paras.get("rS") + "]^{UIMM, 16��b0} < 0, "
						+ "GPR[" + paras.get("rS") + "]^{UIMM, 16��b0} > 0, "
						+ "GPR[" + paras.get("rS") + "]^{UIMM, 16��b0} = 0, "
						+ "XER.SO}");
				break;
				
			case "eqv":
			case "eqv.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"~(GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "])");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]", 
							"{~(GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "]) < 0, "
							+ "~(GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "]) > 0, "
							+ "~(GPR[" + paras.get("rS") + "]^GPR[" + paras.get("rB") + "]) = 0, "
							+ "XER.SO}");
				}
				break;
			
			
			//2.2 �Ӽ�����	
			case "add":
			case "add.":
			case "addo":
			case "addo.":
				map.put("GPR[" + paras.get("rD") + "]", 
						"GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]");
				if(paras.get("OE") == "1") {
					map.put("XER", 
							"{XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") +"]), " + 
							"OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])}");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] > 0, " 
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])}");
				}
				break;
				
			case "addc":
			case "addc.":
			case "addco":
			case "addco.":
				map.put("GPR[" + paras.get("rD") + "]", 
						"GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]");
				map.put("XER.CA", 
						"Carry(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])");
					map.put("XER.OV",
							"OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "])}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "adde":
			case "adde.":
			case "addeo":
			case "addeo.":
				map.put("GPR[" + paras.get("rD") + "]", 
						"GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA");
				map.put("XER.CA", 
						"Carry(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
					map.put("XER.OV",
							"OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA = 0, "
							+ "XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "addi":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16{SIMM[16]}, SIMM}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}");
				}
				break;
				
			case "addic":
			case "addic.":
				map.put("GPR[" + paras.get("rD") + "]",
						"GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}");
				map.put("XER.CA",
						"GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}");
				break;
				
			case "addis":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]",
							"{SIMM, 16��b0}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"GPR[" + paras.get("rA") + "]+{SIMM, 16��b0}");
				}
				break;
				
			case "addme":
			case "addme.":
			case "addmeo":
			case "addmeo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA");
				map.put("XER.CA", 
						"Carry(GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
					map.put("XER.OV",
							"OverFlow(GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA = 0, "
							+ "XER.SO|OverFlow(GPR[rA]+0xFFFFFFFF+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "addze":
			case "addze.":
			case "addzeo":
			case "addzeo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"GPR[" + paras.get("rA") + "]+XER.CA");
				map.put("XER.CA", 
						"Carry(GPR[" + paras.get("rA") + "]+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(GPR[" + paras.get("rA") + "]+XER.CA)");
					map.put("XER.OV",
							"OverFlow(GPR[" + paras.get("rA") + "]+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+XER.CA = 0, "
							+ "XER.SO|OverFlow(GPR[rA]+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{GPR[" + paras.get("rA") + "]+XER.CA < 0, "
							+ "GPR[" + paras.get("rA") + "]+XER.CA > 0, "
							+ "GPR[" + paras.get("rA") + "]+XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "neg":
			case "neg.":
			case "nego":
			case "nego.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+1");
				
				if(paras.get("OE") == "1" && ("GPR[" + paras.get("rA") + "]") == "0x8000_0000") {
					map.put("XER.SO",
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+1)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+1)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1" 
						&& ("GPR[" + paras.get("rA") + "]") == "0x8000_0000") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+1 = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+1)}");
				}
				if(paras.get("Rc") == "1" && !(paras.get("OE") == "1" 
						&& ("GPR[" + paras.get("rA") + "]") == "0x8000_0000")) {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+1 = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "subf":
			case "subf.":
			case "subfo":
			case "subfo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "subfc":
			case "subfc.":
			case "subfco":
			case "subfco.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1");
				map.put("XER.CA", 
						"Carry(~GPR[" + paras.get("rA") + "]+GPR["+ paras.get("rB") + "]+1)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 < 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 > 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+1 = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "subfe":
			case "subfe.":
			case "subfeo":
			case "subfeo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA");
				map.put("XER.CA", 
						"Carry(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA < 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA > 0, "
							+ "~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "]+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{~GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "] +XER.CA< 0, "
							+ "~GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "] +XER.CA> 0, "
							+ "~GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "] +XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
				
				
			case "subfic":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}+1");
				map.put("XER.CA", 
						"Carry(~GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}+1)");
			break;
			
			case "subfme":
			case "subfme.":
			case "subfmeo":
			case "subfmeo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA");
				map.put("XER.CA", 
						"Carry(~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA < 0, "
							+ "~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA > 0, "
							+ "~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA < 0, "
							+ "~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA > 0, "
							+ "~GPR[" + paras.get("rA") + "]+0xFFFFFFFF+XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
			
			case "subfze":
			case "subfze.":
			case "subfzeo":
			case "subfzeo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"~GPR[" + paras.get("rA") + "]+XER.CA");
				map.put("XER.CA", 
						"Carry31(~GPR[" + paras.get("rA") + "]+XER.CA)");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+XER.CA)");
					map.put("XER.OV",
							"OverFlow(~GPR[" + paras.get("rA") + "]+XER.CA)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{~GPR[" + paras.get("rA") + "]+XER.CA < 0, "
							+ "~GPR[" + paras.get("rA") + "]+XER.CA > 0, "
							+ "~GPR[" + paras.get("rA") + "]+XER.CA = 0, "
							+ "XER.SO|OverFlow(~GPR[" + paras.get("rA") + "]+XER.CA)}"); 
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{~GPR[" + paras.get("rA") + "]+XER.CA < 0, "
							+ "~GPR[" + paras.get("rA") + "]+XER.CA > 0, "
							+ "~GPR[" + paras.get("rA") + "]+XER.CA = 0, "
							+ "XER.SO}");
				}
				break;
			
			
			//2.3 �˳�������	
			case "divw":
			case "divw.":
			case "divwo":
			case "divwo.":
				if((("GPR[" + paras.get("rA") + "]" == "-231") 
						& ("GPR[" + paras.get("rB") + "]" == "-1")) 
						| ("GPR[" + paras.get("rB") + "]" == "0")) {
					map.put("GPR[" + paras.get("rD") + "]",
							"undefined");
					if(paras.get("OE") == "1") {
						map.put("XER", "{1,1}");
					}
					if(paras.get("Rc") == "1") {
						map.put("CR[0]", "{undefined, undefined, undefined, 1}");
					}
					
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "]");
					if(paras.get("OE") == "1") {
						map.put("XER", "{XER.SO, 0}");
					}
					if(paras.get("Rc") == "1") {
						map.put("CR[0]", 
								"{GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] < 0, "
								+ "GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] > 0, "
								+ "GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] = 0, "
								+ "XER.SO}");
					}
				}
				break;
				
			case "divwu":
			case "divwu.":
			case "divwuo":
			case "divwuo.":
				if("GPR[" + paras.get("rB") +"]" == "0") {
					map.put("GPR[" + paras.get("rD") + "]",
							"undefined");
					if(paras.get("OE") == "1") {
						map.put("XER", "{1,1}");
					}
					if(paras.get("Rc") == "1") {
						map.put("CR[0]", "{undefined, undefined, undefined, 1}");
					}
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "]");
					if(paras.get("OE") == "1") {
						map.put("XER", "{XER.SO, 0}");
					}
					if(paras.get("Rc") == "1") {
						map.put("CR[0]", 
								"{GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] < 0, "
								+ "GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] > 0, "
								+ "GPR[" + paras.get("rA") + "]/GPR[" + paras.get("rB") + "] = 0, "
								+ "XER.SO}");
					}
				}
				break;
			
			case "mulhw":
			case "mulhw.":
				map.put("GPR[" + paras.get("rD") + "]",
						"(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "])[0:31]");
				if(paras.get("Rc") == "1") {
					map.put("CR[0]",
							"{GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
				
			case "mulhwu":
			case "mulhwu.":
				map.put("GPR[" + paras.get("rD") + "]",
						"(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "])[0:31]");
				map.put("CR[0]",
						"{GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] < 0, "
						+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] > 0, "
						+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] = 0, "
						+ "XER.SO}");
				break;
				
			case "mulli":
				map.put("GPR[" + paras.get("rD") + "]",
						"(GPR[" + paras.get("rA") + "]*{16{SIMM[16]}, SIMM})[16:48]");
				break;
						
			case "mullw":
			case "mullw.":
			case "mullwo":
			case "mullwo.":
				map.put("GPR[" + paras.get("rD") + "]",
						"(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "])[32:63]");
				if(paras.get("OE") == "1") {
					map.put("XER.SO", 
							"XER.SO|(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=320)&(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=321)");
					map.put("XER.OV", 
							"(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=320)&(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=321)");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "1") {
					map.put("CR[0]", 
							"{GPR[" + paras.get("rA") + "]*GPR[rB] < 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO|(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=320)&(GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "]!=321)}");
				}
				if(paras.get("Rc") == "1" && paras.get("OE") == "0") {
					map.put("CR[0]",
							"{GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] < 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] > 0, "
							+ "GPR[" + paras.get("rA") + "]*GPR[" + paras.get("rB") + "] = 0, "
							+ "XER.SO}");
				}
				break;
				
			
			//2.4 ��֧��ת
			case "b":
			case "ba":
			case "bl":
			case "bla":
				if(paras.get("AA") == "1") {
					map.put("PC", 
							"640+{6{" + paras.get("LI") + "[0]}, "
							+ paras.get("LI") + ", "
							+ "2��b0}");
				} else {
					map.put("PC", 
							"PC+{6{" + paras.get("LI") + "[0]}, "
							+ paras.get("LI") + ", "
							+ "2��b0}");
				}
				if(paras.get("LK") == "1") {
					map.put("LR",
							"PC+4");
				}
				break;
				
			case "bc":
			case "bca":
			case "bcl":
			case "bcla":
				if(paras.get("BO") + "[2]" == "0") {
					map.put("CTR", "CTR-1");
					map.put("ctr_ok", 
							paras.get("BO") + "[2]||((CTR-1!=0)^" + paras.get("BO") + "[3])");
				} else {
					map.put("ctr_ok", 
							paras.get("BO") + "[2]||((CTR!=0)^" + paras.get("BO") + "[3])");
				}
				map.put("cond_ok", 
						paras.get("BO") + "[0]||(CR[" + paras.get("BI") + "]==" + paras.get("BO") + "[1])");
				if("ctr_ok&cond_ok" == "1") {
					if(paras.get("AA") == "1") {
						map.put("PC",
								"(640+{16{" + paras.get("BD") + "[0]}, "
								+ paras.get("BD") + ", "
								+ "2��b0})");
					} else {
						map.put("PC",
								"(PC+{16{" + paras.get("BD") + "[0]}, "
								+ paras.get("BD") + ", "
								+ "2��b0})");
					}
				} else {
					map.put("PC", "PC + 4");
				}
				if(paras.get("LK") == "1") {
					map.put("LR", "PC+4");
				}
				break;
				
			case "bcctr":
			case "bcctrl":
				if(paras.get("BO") + "[0]" == "0" 
					||("CR[" + paras.get("BI") + "]" == paras.get("BO") + "[1])")) {
					map.put("PC", "PC+4");
				} else {
					map.put("PC", "{CTR[0:29], 2��b0}");
				}
				if(paras.get("LK") == "1") {
					map.put("LR", "PC+4");
				}
				break;
				
			case "bclr":
			case "bclrl":
				if(paras.get("BO") + "[2]" == "0") {
					map.put("CTR", "CTR-1");
					map.put("ctr_ok", 
							paras.get("BO") + "[2]||((CTR-1!=0)^" + paras.get("BO") + "[3])");
				} else {
					map.put("ctr_ok", 
							paras.get("BO") + "[2]||((CTR!=0)^" + paras.get("BO") + "[3])");
				}
				map.put("cond_ok", 
						paras.get("BO") + "[0]|(CR[" + paras.get("BI") + "]==" + paras.get("BO") + "[1])");
				if(map.get("ctr_ok")=="1" & map.get("cond_ok")=="1") {
					map.put("PC", "{LR[0:29],2'b00}");
				} else {
					map.put("PC", "PC+4");
				}
				if(paras.get("LK") == "1") {
					map.put("LR", "PC+4");
				}
				break;
			
				
			//2.5 �Ƚ�
			case "cmp":
				if (("GPR[" + paras.get("rA") + "]").compareTo("GPR[" + paras.get("rB") + "]") == -1) {  //compareTo(), 0 ���; 1 ����; -1 С��
					map.put("CR[" + paras.get("crfD") + "]", "{3��b100, XER.SO}");
				} else if (("GPR[" + paras.get("rA") + "]").compareTo("GPR[" + paras.get("rB") + "]") == 1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b010, XER.SO}");
				} else {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b001, XER.SO}");
				}
				break;
				
			case "cmpi":
				if (("GPR[" + paras.get("rA") + "]").compareTo("{16{SIMM[16]}, SIMM}") == -1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b100, XER.SO}");
				} else if (("GPR[" + paras.get("rA") + "]").compareTo("{16{SIMM[16]}, SIMM}") == 1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b010, XER.SO}");
				} else {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b001, XER.SO}");
				}
				break;
			
			case "cmpl":
				if (("GPR[" + paras.get("rA") + "]").compareTo("u  GPR[" + paras.get("rB") + "]") == -1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b100, XER.SO}");
				} else if (("GPR[" + paras.get("rA") + "]").compareTo("u  GPR[" + paras.get("rB") + "]") == 1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b010, XER.SO}");
				} else {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b001, XER.SO}");
				}
				break;
				
			case "cmpli":
				if (("GPR[" + paras.get("rA") + "]").compareTo("u  {16��b0, SIMM}") == -1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b100, XER.SO}");
				} else if (("GPR[" + paras.get("rA") + "]").compareTo("u  {16��b0, SIMM}") == 1) {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b010, XER.SO}");
				} else {
					map.put("CR[" + paras.get("crfD") + "]", "{3��b001, XER.SO}");
				}
				break;
			
				
			//2.6 λ��	
			case "slw":
			case "slw.":
				if("(GPR[" + paras.get("rB") + "])[26]" == "0") {
					map.put("GPR[" + paras.get("rA") + "]", 
							"(GPR[" + paras.get("rS") + "]<<(GPR[" + paras.get("rB") + "])[27:31])&{(31-n+1}{1��b1),n{1��b0}}");
				} else {
					map.put("GPR[" + paras.get("rA") + "]", "0");
				}
				break;
				
			case "sraw":
			case "sraw.":
				if("(GPR[" + paras.get("rB") + "])[26]" == "0") {
					map.put("GPR[" + paras.get("rA") + "]", 
							"((GPR[" + paras.get("rS") + "]<<32-(GPR[" + paras.get("rB") + "])[27:31])&{( n-1}{1��b0), "
							+ "(32-n){1��b1}})|(32{" + paras.get("rS") + "[0]}&~{( n-1}{1��b0), "
							+ "(32-n){1��b1}})");
				} else {
					map.put("GPR[" + paras.get("rA") + "]", 
							"((GPR[" + paras.get("rS") + "]<<32-(GPR[" + paras.get("rB") + "])[27:31])&32��b0)|(32{" + paras.get("rS") + "[0]}&32��hFFFFFFFF)");
				}
				map.put("XER.CA", paras.get("rS") + "[0]"
						+ "||((GPR[" + paras.get("rS") + "]<<32-(GPR[" + paras.get("rB") + "])[27:31])&~{( n-1}{1��b0),(32-n){1��b1}} �� 0)");
				break;
				
			case "srawi":
			case "srawi.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"((GPR[" + paras.get("rS") + "]<<32-SH)&{( n-1}{1��b0),(32-n){1��b1}})"
						+ "|(32{" + paras.get("rS") + "[0]}&~{( n-1}{1��b0),(32-n){1��b1}})");
				map.put("XER.CA", 
						paras.get("rS") + "[0]"
						+ "||((GPR[" + paras.get("rS") + "]<<32-(GPR[" + paras.get("rB") + "])[27:31])&~{( n-1}{1��b0),(32-n){1��b1}} �� 0)");
				break;
				
			case "srw":
			case "srw.":
				if("(GPR[" + paras.get("rB") + "])[26]" == "0") {
					map.put("GPR[" + paras.get("rA") + "]", 
							"(GPR[" + paras.get("rS") + "]<<32-(GPR[" + paras.get("rB") + "])[27:31])&{(n-1}{1��b0),(32-n){1��b1}}");
				} else {
					map.put("GPR[" + paras.get("rA") + "]", "0");
				}
				break;
				
			case "rlwimi":
			case "rlwimi.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"((GPR[" + paras.get("rS") + "]<<SH)&{(MB-1}{1��b0), (ME-MB+1){1��b1}, (32-ME){1��b0}})"
						+ "|(GPR[" + paras.get("rS") + "]&~{(MB-1}{1��b0), (ME-MB+1){1��b1}, (32-ME){1��b0}})");
				break;
				
			case "rlwinm":
			case "rlwinm.":
				map.put("GPR[" + paras.get("rA") + "]", 
						"((GPR[" + paras.get("rS") + "]<<SH)&{(MB-1}{1��b0), (ME-MB+1){1��b1}, (32-ME){1��b0}})");
				break;
				
			case "rlwnm":
			case "rlwnm.":
				map.put("GPR[" + paras.get("rA") + "]",
						"((GPR[" +  paras.get("rS") + "]<<(GPR[" + paras.get("rB") + "])[27:31])&{(MB-1}{1��b0), "
						+ "(ME-MB+1){1��b1}, (32-ME){1��b0}})|(GPR[" + paras.get("rS") + "]&~{(MB-1}{1��b0), "
						+ "(ME-MB+1){1��b1},(32-ME){1��b0}})");
				break;
				
			
			//2.7 ȡ�ֽڡ����ֺ���
			case "lbz":
				if( paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{24��b0, MEM({16{SIMM[16]}, SIMM},1)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{24��b0, MEM(GPR[" + paras.get("rA") + "]+{16{SIMM[16]}, SIMM}, 1)}");
				}
				break;
				
			case "lbzu":
				map.put("GPR[" + paras.get("rD") + "]", 
						"{24��b0,MEM(GPR[" + paras.get("rA") + "]+{16{SIMM[16]},SIMM},1)}");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
				
			case "lbzux":
				map.put("GPR[" + paras.get("rD") + "]", 
						"{24��b0,MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "], 1)}");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
			
			case "lbzx":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{24��b0, MEM(GPR[" + paras.get("rB") + "], 1)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{24��b0, MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "], 1)}");
				}
				break;
				
			case "lha":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{16{(MEM({16{SIMM[16]}, SIMM},2))[0]},MEM({16{SIMM[16]}, SIMM},2)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16{(MEM(GPR[" + paras.get("rA") + "] +{16{SIMM[16]}, SIMM},2))[0]},"
							+ "MEM(GPR[" + paras.get("rA") + "] +{16{SIMM[16]}, SIMM},2)}");
				}
				break;
				
			case "lhau":
				map.put("GPR[" + paras.get("rD") + "]", 
						"{16{(MEM({16{SIMM[16]}, SIMM},2))[0]},MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},2)}");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
				
			case "lhaux":
				map.put("GPR[" + paras.get("rD") + "]", 
						"{16{(MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],2)}[0]}, "
						+ "MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],2)}");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
				
			case "lhax":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{16{(MEM(GPR[" + paras.get("rB") + "],2))[0]}, MEM(GPR[" + paras.get("rB") + "],2)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16{(MEM(GPR[" + paras.get("rA") + "] +GPR[" + paras.get("rB") + "],2))[0]}, "
							+ "MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],2)}");
				}
				break;
				
			case "lhbrx":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{16��b0, MEM(GPR[" + paras.get("rB") + "]+1,1), MEM(GPR[" + paras.get("rB") + "],1)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16��b0, MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]+1,1), "
							+ "MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],1)}");
				}
				break;
				
			case "lhz":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{16��b0, MEM({16{SIMM[16]}, SIMM},2)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16��b0, MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},2)}");
				}
				break;
				
			case "lhzu":
				map.put("GPR[" + paras.get("rD") + "]",
						"{16��b0,MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},1)}");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
				
			case "lhzux":
				map.put("GPR[" + paras.get("rD") + "]",
						"{16��b0, MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "], 2)}");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
				
			case "lhzx":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{16��b0,MEM(GPR[" + paras.get("rB") + "],2)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{16��b0,MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],2)}");
				}
				break;
				
			case "lwbrx":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"{MEM(GPR[" + paras.get("rB") + "]+3,1), MEM(GPR[" + paras.get("rB") + "]+2,1), "
							+ "MEM(GPR[" + paras.get("rB") + "]+1,1), MEM(GPR[" + paras.get("rB") + "],1)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"{MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]+3,1), MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]+2,1), "
							+ "MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]+1,1), MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],1)}");
				}
				break;
				
			case "lwz":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"MEM({16{SIMM[16]}, SIMM},4)}");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},4)}");
				}
				break;
				
			case "lwzu":
				map.put("GPR[" + paras.get("rD") + "]",
						"MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},4)");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
				
			case "lwzux":
				map.put("GPR[" + paras.get("rD") + "]",
						"MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],4)");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
				
			case "lwzx":
				if(paras.get("rA") == "0") {
					map.put("GPR[" + paras.get("rD") + "]", 
							"MEM(GPR[" + paras.get("rB") + "],4)");
				} else {
					map.put("GPR[" + paras.get("rD") + "]",
							"MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],4)");
				}
				break;
				
			//ָ������δ����	
			case "dsn":
			case "lbdx":
			case "lhdx":
			case "lwdx":
			case "lwarx":
			case "lmw":
			case "cntlzw":
			case "cntlzw.":
				break;
			
			
			//2.8 ���ֽڡ����ֺ���	
			case "stb":
				if(paras.get("rA") == "0") {
					map.put("MEM({16{SIMM[16]}, SIMM}, 1)", 
							"(GPR[" + paras.get("rS") + "])[24:31]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}, 1)", 
							"(GPR[" + paras.get("rS") + "])[24:31]");
				}
				break;
				
			case "stbu":
				map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},1)",
						"(GPR[" + paras.get("rS") + "])[24:31]");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
				
			case "stbux":
				map.put("MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],1)", 
						"(GPR[" + paras.get("rS") + "])[24:31]");
				map.put("GPR[" + paras.get("rA") + "]",
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
				
			case "stbx":
				if(paras.get("rA") == "0") {
					map.put("MEM(GPR[" + paras.get("rB") + "], 1)", 
							"(GPR[" + paras.get("rS") + "])[24:31]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "],1)", 
							"(GPR[" + paras.get("rS") + "])[24:31]");
				}
				break;
				
			case "sth":
				if(paras.get("rA") == "0") {
					map.put("MEM({16{SIMM[16]}, SIMM}, 2)", 
							"(GPR[" + paras.get("rS") + "])[16:31]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}, 2)", 
							"(GPR[" + paras.get("rS") + "])[16:31]");
				}
				break;
				
			case "sthbrx":
				if(paras.get("rA") == "0") {
					map.put("MEM(GPR[" + paras.get("rB") + "],2)", 
							"{(GPR[" + paras.get("rB") + "])[24:31], (GPR[" + paras.get("rB") + "])[16:23]}");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "],2)", 
							"{(GPR[" + paras.get("rB") + "])[24:31], (GPR[" + paras.get("rB") + "])[16:23]}");
				}
				break;
			
			case "sthu":
				map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},2)",
						"(GPR[" + paras.get("rS") + "])[16:31]");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
			
			case "sthux":
				map.put("MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],2)", 
						"(GPR[" + paras.get("rS") + "])[16:31]");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
				
			case "sthx":
				if(paras.get("rA") == "0") {
					map.put("MEM(GPR[" + paras.get("rB") + "],2)",
							"(GPR[" + paras.get("rS") + "])[16:31]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "],2)",
							"(GPR[" + paras.get("rS") + "])[16:31]");
				}
				break;
				
			case "stw":
				if(paras.get("rA") == "0") {
					map.put("MEM({16{SIMM[16]}, SIMM}, 4)",
							"GPR[" + paras.get("rS") + "]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}, 4)",
							"GPR[" + paras.get("rS") + "]");
				}
				break;
			
			case "stwbrx":
				if(paras.get("rA") == "0") {
					map.put("EM(GPR[" + paras.get("rB") + "],4)",
							"{(GPR[" + paras.get("rB") + "])[24:31], (GPR[" + paras.get("rB") + "])[16:23], "
							+ "(GPR[" + paras.get("rB") + "])[8:15], (GPR[" + paras.get("rB") + "])[0:7]}");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "],4)",
							"{(GPR[" + paras.get("rB") + "])[24:31], (GPR[" + paras.get("rB") + "])[16:23], "
							+ "(GPR[" + paras.get("rB") + "])[8:15], (GPR[" + paras.get("rB") + "])[0:7]}");
				}
				break;
			
			case "stwu":
				map.put("MEM(GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM},4)", 
						"GPR[" + paras.get("rS") + "]");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + {16{SIMM[16]}, SIMM}");
				break;
			
			case "stwux":
				map.put("MEM(GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "],4)", 
						"GPR[" + paras.get("rS") + "]");
				map.put("GPR[" + paras.get("rA") + "]", 
						"GPR[" + paras.get("rA") + "] + GPR[" + paras.get("rB") + "]");
				break;
			
			case "stwx":
				if(paras.get("rA") == "0") {
					map.put("MEM(GPR[" + paras.get("rB") + "],14)",
							"GPR[" + paras.get("rS") + "]");
				} else {
					map.put("MEM(GPR[" + paras.get("rA") + "]+GPR[" + paras.get("rB") + "],4)",
							"GPR[" + paras.get("rS") + "]");
				}
				break;
			
			//δ���
			case "stbdx":
			case "stfddx":
			case "sthdx":
			case "stwdx":
			case "stmw":
			case "stwcx.":
				break;
			
				
			//2.9 ����	
			case "tw":
			case "twi":
				break;
			
				
			//2.10 �����Ĵ����߼�����
			case "crand":
				map.put("CR[" + paras.get("crbD") + "]", 
						"CR[" + paras.get("crbA") + "] & CR[" + paras.get("crbB") + "]");
				break;
			
			case "crandc":
				map.put("CR[" + paras.get("crbD") + "]", 
						"CR[" + paras.get("crbA") + "] & ~CR[" + paras.get("crbB") + "]");
				break;
			
			case "creqv":
				map.put("CR[" + paras.get("crbD") + "]", 
						"CR[" + paras.get("crbA") + "] ^ CR[" + paras.get("crbB") + "]");
				break;
			
			case "crnand":
				map.put("CR[" + paras.get("crbD") + "]", 
						"~(CR[" + paras.get("crbA") + "] & CR[" + paras.get("crbB") + "])");
				break;
				
			case "crnor":
				map.put("CR[" + paras.get("crbD") + "]", 
						"~(CR[" + paras.get("crbA") + "] | CR[" + paras.get("crbB") + "])");
				break;
				
			case "cror":
				map.put("CR[" + paras.get("crbD") + "]", 
						"CR[" + paras.get("crbA") + "] | CR[" + paras.get("crbB") + "]");
				break;
				
			case "crxor":
				map.put("CR[" + paras.get("crbD") + "]", 
						"CR[" + paras.get("crbA") + "] ^ CR[" + paras.get("crbB") + "]");
				break;
				
				
			//2.11 �ƶ�����Ĵ���
			case "mcrf":
				map.put("CR[" + paras.get("crbD") + "])", 
						"CR[" + paras.get("crbS") + "]");
				break;
				
			case "mcrxr":
				map.put("CR[" + paras.get("crfD") + "]",
						"XER[0:3]");
				map.put("XER[0:3]", 
						"4��b0");
				break;
				
			case "mfcr":
				map.put("GPR[" + paras.get("rD") + "]", 
						"CR");
				break;
				
			case "mfmsr":
				map.put("GPR[" + paras.get("rD") + "]", 
						"MSR");
				break;
			
			case "mfspr":
				map.put("GPR[" + paras.get("rD") + "]", 
						"SPR[{SPRn[5:9],SPRn[0:4]}]");
				break;
				
			//δ���	
			case "mftb":
			case "mtcrf":
			case "mtspr":
			case "mtmsr":	
			case "mfocrf":
			case "mtocrf":
			case "isel":
			case "wrtee":
			case "wrteei":
			case "msgclr":
			case "msgsnd":
			case "ehpriv":
			case "mbar":
				break;
				
			
			//2.12 ϵͳ���á�ͬ�����ж�
			case "sc":
			case "sync":
			case "isync":
			case "wait":
			case "rfci":
			case "rfdi":
			case "rfgi":
			case "rfi":
			case "rfmci":
				break;
			
			
			//2.13 ���ٻ���
			case "dcba":
			case "dcbal":
			case "dcbf":
			case "dcbi":
			case "dcblc":
			case "dcbst":
			case "dcbt":
			case "dcbtls":
			case "dcbtstls":
			case "dcbz":
			case "dcbzep":
			case "dcbzl":
			case "dcbzlep":
			case "icbi":
			case "icbt":
			case "icblc":
			case "icbtls":
				break;
			
				
			//2.14 MMU
			case "dcbfep":
			case "dcbstep":
			case "dcbtep":
			case "dcbtstep":
			case "icbiep":
			case "lbepx":
			case "lhepx":
			case "lwepx":
			case "stbepx":
			case "stfdepx":
			case "sthepx":
			case "stwepx":
			case "tlbilx":
			case "tlbivax":
			case "tlbsync":
			case "tlbsx":
			case "tlbre":
			case "tlbwe":	
				break;
			
				
			//2.15 ����
			case "dnh":
				break;
				
			default: 
				break;
		}
		
		return map;
	}
	
	//������������ָ��
	public void resolve(String line) {
		String[] strArray =  line.split(" |,|;|\\(|\\)");
		List<String> strList = new ArrayList<String>();
		for(int i=0;i<strArray.length;i++) {
			if(strArray[i].length()==0 || strArray[i]=="" 
					|| strArray[i]==" " || strArray[i]=="\t" 
					|| strArray[i]==";") {
				continue;
			} else {
				strList.add(strArray[i]);
			}
		}

		this.setName(strList.get(0));
		this.setParas(addMarkBit(this.getName(), analyzeParas(strList)));
		this.setSemantic(addSemantic(this.getName(), this.getParas()));
	}
	
}
 