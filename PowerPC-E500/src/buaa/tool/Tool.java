package buaa.tool;

import java.util.Iterator;
import java.util.List;

import buaa.data.Instruction;

public class Tool {
	public static boolean check1Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=1) return true;
		else return false;
	}
	
	public static boolean check2Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=3) return true;
		else return false;
	}
	
	public static boolean check3Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=7) return true;
		else return false;
	}
	
	public static boolean check4Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=15) return true;
		else return false;
	}
	
	public static boolean check5Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=31) return true;
		else return false;
	}
	
	public static boolean check8Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=255) return true;
		else return false;
	}
	
	public static boolean check10Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=1023) return true;
		else return false;
	}
	
	public static boolean check14Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=16383) return true;
		else return false;
	}
	
	public static boolean check15Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=32767) return true;
		else return false;
	}
	
	public static boolean check16Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=65535) return true;
		else return false;
	}
	
	public static boolean check16BitSigned(Object str){
		int num = Integer.parseInt((String)str);
		if(-32768<=num && num<=32767) return true;
		else return false;
	}
	
	public static boolean check24Bit(Object str){
		int num = Integer.parseInt((String)str);
		if(0<=num && num<=16777215) return true;
		else return false;
	}
	
	public static boolean checkInstruction(Instruction ins) {
		switch(ins.getName()) {
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
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "andi.":
			case "andis.":
			case "ori":
			case "oris":
			case "xori":
			case "xoris":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check16Bit(ins.getParas().get("UIMM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
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
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "addi":
			case "addic":
			case "addic.":
			case "addis":
			case "subfic":
		
			case "mulli":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check16BitSigned(ins.getParas().get("SIMM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
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
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "b":
			case "ba":
			case "bl":
			case "bla":
				if(!check24Bit(ins.getParas().get("LI"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "bc":
			case "bca":
			case "bcl":
			case "bcla":
				if(!(check5Bit(ins.getParas().get("BO")) && 
						check5Bit(ins.getParas().get("BI")) &&
						check14Bit(ins.getParas().get("BD")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "bcctr":
			case "bcctrl":
			case "bclr":
			case "bclrl":
				if(!(check5Bit(ins.getParas().get("BO")) && 
						check5Bit(ins.getParas().get("BI")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "cmp":
			case "cmpl":
				if(!(check3Bit(ins.getParas().get("crfD")) && 
						check1Bit(ins.getParas().get("L")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "cmpi":
				if(!(check3Bit(ins.getParas().get("crfD")) && 
						check1Bit(ins.getParas().get("L")) &&
						check5Bit(ins.getParas().get("rA")) &&
						check16BitSigned(ins.getParas().get("SIMM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "cmpli":
				if(!(check3Bit(ins.getParas().get("crfD")) && 
						check1Bit(ins.getParas().get("L")) &&
						check5Bit(ins.getParas().get("rA")) &&
						check16Bit(ins.getParas().get("UIMM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "slw":
			case "slw.":
			case "sraw":
			case "sraw.":
			case "srw":
			case "srw.":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
				
			case "srawi":
			case "srawi.":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check5Bit(ins.getParas().get("rH")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "rlwimi":
			case "rlwimi.":
			case "rlwinm":
			case "rlwinm.":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check5Bit(ins.getParas().get("SH")) &&
						check5Bit(ins.getParas().get("MB")) &&
						check5Bit(ins.getParas().get("ME")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "rlwnm":
			case "rlwnm.":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")) &&
						check5Bit(ins.getParas().get("rB")) &&
						check5Bit(ins.getParas().get("MB")) &&
						check5Bit(ins.getParas().get("ME")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "lbz":
			case "lbzu":
			case "lha":
			case "lhau":
			case "lhz":
			case "lhzu":
			case "lwz":
			case "lwzu":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check16Bit(ins.getParas().get("d")) &&
						check5Bit(ins.getParas().get("rA")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "lmw":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check16Bit(ins.getParas().get("D")) &&
						check5Bit(ins.getParas().get("rA")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
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
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "dsn":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "cntlzw":
			case "cntlzw.":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rS")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "stb":
			case "stbu":
			case "sth":
			case "sthu":
			case "stw":
			case "stwu":
				if(!(check5Bit(ins.getParas().get("rS")) && 
						check16Bit(ins.getParas().get("d")) &&
						check5Bit(ins.getParas().get("rA")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "stmw":
				if(!(check5Bit(ins.getParas().get("rS")) && 
						check16Bit(ins.getParas().get("D")) &&
						check5Bit(ins.getParas().get("rA")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
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
				if(!(check5Bit(ins.getParas().get("rS")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
				
			case "stfddx":
				if(!(check5Bit(ins.getParas().get("frS")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "tw":
				if(!(check5Bit(ins.getParas().get("TO")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "twi":
				if(!(check5Bit(ins.getParas().get("TO")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check16BitSigned(ins.getParas().get("SIMM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "crand":
			case "crandc":
			case "creqv":
			case "crnand":
			case "crnor":
			case "cror":
			case "crxor":
				if(!(check5Bit(ins.getParas().get("crbD")) && 
						check5Bit(ins.getParas().get("crbA")) &&
						check5Bit(ins.getParas().get("crbB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
		
			case "mcrf":
				if(!(check3Bit(ins.getParas().get("crfD")) && 
						check3Bit(ins.getParas().get("crfS")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mcrxr":
				if(!check3Bit(ins.getParas().get("crfD"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mfcr":
			case "mfmsr":
				if(!check5Bit(ins.getParas().get("rD"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mfspr":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check10Bit(ins.getParas().get("SPRN")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mftb":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check10Bit(ins.getParas().get("TBRN")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mtcrf":
				if(!(check8Bit(ins.getParas().get("CRM")) && 
						check5Bit(ins.getParas().get("rS")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mtspr":
				if(!(check10Bit(ins.getParas().get("SPRN")) && 
						check5Bit(ins.getParas().get("rS")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mtmsr":
				if(!check5Bit(ins.getParas().get("rS"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
				
			case "mfocrf":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check8Bit(ins.getParas().get("FXM")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
				
			case "isel":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")) &&
						check5Bit(ins.getParas().get("crb")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
				
			case "wrtee":
				if(!check5Bit(ins.getParas().get("rS"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "wrteei":
				if(!check1Bit(ins.getParas().get("E"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "msgclr":
			case "msgsnd":
				if(!check5Bit(ins.getParas().get("rB"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "ehpriv":
				if(!check15Bit(ins.getParas().get("OC"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "mbar":
				if(!check5Bit(ins.getParas().get("MO"))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "sc":
			case "isync":
			case "rfdi":
			case "rfgi":
			case "rfi":
			case "rfmci":
				break;
				
			case "sync":
				if(!(check2Bit(ins.getParas().get("L")) && 
						check4Bit(ins.getParas().get("E")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "wait":
				if(!(check2Bit(ins.getParas().get("WC")) && 
						check1Bit(ins.getParas().get("WH")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
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
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "dcblc":
			case "dcbt":
			case "dcbtls":
			case "dcbtstls":
			case "icbt":
			case "icblc":
			case "icbtls":
				if(!(check5Bit(ins.getParas().get("CT")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			
			case "dcbfep":
			case "dcbstep":
			case "icbiep":
			case "tlbivax":
			case "tlbsx":
				if(!(check5Bit(ins.getParas().get("rA")) && 
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "dcbtep":
			case "dcbtstep":
				if(!(check5Bit(ins.getParas().get("TH")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "lbepx":
			case "lhepx":
			case "lwepx":
				if(!(check5Bit(ins.getParas().get("rD")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "tlbre":
				break;
			case "stbepx":
			case "sthepx":
			case "stwepx":
				if(!(check5Bit(ins.getParas().get("rS")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "stfdepx":
				if(!(check5Bit(ins.getParas().get("frS")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "tlbilx":	
				if(!(check2Bit(ins.getParas().get("T")) && 
						check5Bit(ins.getParas().get("rA")) &&
						check5Bit(ins.getParas().get("rB")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			case "tlbsync":
			case "tlbwe":
				break;
		
			case "dnh":
				if(!(check5Bit(ins.getParas().get("DUI")) && 
						check5Bit(ins.getParas().get("DCTL")))) {
					System.out.println(ins.getName() + "Ö¸Áî²Ù×÷Êý·¶Î§´íÎó£¡");
					return false;
				}
				break;
			default: break;
		}
		return true;
	}
	
	public static boolean check(List<Instruction> sourceCode) {
		boolean flag = true;
		Iterator<Instruction> iter = sourceCode.iterator();
		while(iter.hasNext()) {
			Instruction ins = iter.next();
			if(!checkInstruction(ins)) {
				flag = false;
			}
		}
		return flag;
	}

}
