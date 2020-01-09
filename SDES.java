/* 
Problem Statement:- Implementation of S-DES 
Made by:- Nishant S. Shrivastav
demo testing serverless
jenkins
*/

class SDES
{
	public static void main(String args[])
	{
		int plainText[] = {0,1,1,0,0,0,1,0};
		System.out.println("\n----------------------------------\n");
		System.out.println("Sender side PlainText is:-");
		for(int i=0;i<8;i++)
			System.out.print(plainText[i]);
		
		int intialKey[] = {1,0,1,0,0,0,0,0,1,0};	
		
		int S0[][] = {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};
		int S1[][] = {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};
		
		//Generate Both Keys k1 and k2
		int key1[] = generateKey1(intialKey);
		int key2[] = generateKey2(intialKey);
		
		int cipherText[] = doEncryption(plainText,key1,key2,S0,S1);
		
		System.out.println("\n----------------------------------\n");
		System.out.println("Sender after encrypting plainText:-");
		for(int i=0;i<8;i++)
			System.out.print(cipherText[i]);
		
			
		int originalText[] = doDecryption(cipherText,key1,key2,S0,S1);
		
		System.out.println("\n----------------------------------\n");
		System.out.println("Receiver after decrypting cipherText:-");
		for(int i=0;i<8;i++)
			System.out.print(originalText[i]);
		
	}
	
	static int[] doEncryption(int plainText[],int key1[],int key2[], int S0[][],int S1[][])
	{
		
		int IP[] = doInitialPermutation(plainText);   //8-bits
		int intermediateText1[] = desFunction(IP,key1,S0,S1);
		int swapOutput[] = doSwap(intermediateText1);
		int intermediateText2[] = desFunction(swapOutput,key2,S0,S1);
		int cipherText[] = doFinalPermutation(intermediateText2);
		
		return cipherText;
	}
	
	static int[] doDecryption(int cipherText[],int key1[],int key2[], int S0[][],int S1[][])
	{
		
		int IP[] = doInitialPermutation(cipherText);   //8-bits
		int intermediateText1[] = desFunction(IP,key2,S0,S1);
		int swapOutput[] = doSwap(intermediateText1);
		int intermediateText2[] = desFunction(swapOutput,key1,S0,S1);
		int plainText[] = doFinalPermutation(intermediateText2);
		
		return plainText;
	}
	
	static int[] generateKey1(int key[])
	{
		int P10key[] = doP10(key);
		int LS1[] = doLeftShift(P10key,1);
		int key1[] = doP8(LS1);
		return key1;
	}
	
	static int[] generateKey2(int key[])
	{
		int P10key[] = doP10(key);
		int LS2[] = doLeftShift(P10key,3);
		int key2[] = doP8(LS2);
		return key2;
	}
	
	
	static int[] doP10(int key[])
	{
		int P10key[] = {key[2],key[4],key[1],key[6],key[3],key[9],key[0],key[8],key[7],key[5]};
		return P10key;
	}
	
	
	static int[] doLeftShift(int P10key[],int shiftby)
	{
		for(int j=0;j<shiftby;j++)
		{
			int firstelement = P10key[0];
			int length = P10key.length;
			int firsthalflength = length/2;
			
			
			for(int i=1;i<firsthalflength;i++)
			{
				P10key[i-1]=P10key[i];
			}
			
			P10key[firsthalflength-1] = firstelement;
			
			int temp = P10key[firsthalflength];
			
			for(int i=firsthalflength+1;i<length;i++)
			{
				P10key[i-1]=P10key[i];
			}
			
			P10key[length-1] = temp;
		}
		
		return P10key;
	}
	
	static int[] doP8(int LS1[])
	{
		int key[] = {LS1[5],LS1[2],LS1[6],LS1[3],LS1[7],LS1[4],LS1[9],LS1[8]};
		return key;
	}
	
	static int[] doInitialPermutation(int plainText[])
	{
		int IP[] = {plainText[1],plainText[5],plainText[2],plainText[0],plainText[3],plainText[7],plainText[4],plainText[6]};
		return IP;
	}
	
	static int[] doExpand(int IP[])
	{
		int ERH[] = {IP[7],IP[4],IP[5],IP[6],IP[5],IP[6],IP[7],IP[4]};
		return ERH;
	}
	
	static int[] doEXOR(int part1[], int part2[])
	{
		int EXORED[] = new int[part1.length];
		
		for(int i=0;i<part1.length;i++)
		{
			EXORED[i]=part1[i]^part2[i];
		}
		return EXORED;
	}
	
	static String convertdecimaltobinary(int n)
	{
		if(n==0)
			return "00";
		else if(n==1)
			return "01";
		else if(n==2)
			return "10";
		else if(n==3)
			return "11";
		
		return "00";
	}
	
	
	static int[] doSBOXOperation(int EXORED[],int S0[][],int S1[][])
	{
		int S0row = Integer.parseInt(String.valueOf(EXORED[0]) + String.valueOf(EXORED[3]),2);
		int S0col = Integer.parseInt(String.valueOf(EXORED[1]) + String.valueOf(EXORED[2]),2);
		int S1row = Integer.parseInt(String.valueOf(EXORED[4]) + String.valueOf(EXORED[7]),2);
		int S1col = Integer.parseInt(String.valueOf(EXORED[5]) + String.valueOf(EXORED[6]),2);
		
		int n1 = S0[S0row][S0col]; 
		int n2 = S1[S1row][S1col];
		
		String x = convertdecimaltobinary(n1);
		String y = convertdecimaltobinary(n2);
		
		
		int SBOXOUTPUT[]={Character.getNumericValue(x.charAt(0)),Character.getNumericValue(x.charAt(1)),Character.getNumericValue(y.charAt(0)),Character.getNumericValue(y.charAt(1))};
		return SBOXOUTPUT;
	}
	
	static int[] doP4(int SBO[])
	{
		int P4[] = {SBO[1],SBO[3],SBO[2],SBO[0]};
		return P4;
	}
	
	static int[] desFunction(int IP[],int key1[],int S0[][],int S1[][])
	{
		int expandedRightHalf[] = doExpand(IP); 
		int EXORED[] = doEXOR(expandedRightHalf, key1);
		int SBOXOUTPUT[] = doSBOXOperation(EXORED,S0,S1);
		int P4[] = doP4(SBOXOUTPUT);
		int leftHalfofIP[] = {IP[0],IP[1],IP[2],IP[3]};
		int LH[] = doEXOR(leftHalfofIP,P4);
		int output[] = {LH[0],LH[1],LH[2],LH[3],IP[4],IP[5],IP[6],IP[7]};
		return output;
	}
	
	static int[] doSwap(int IT[])
	{
		int swapOutput[] = {IT[4],IT[5],IT[6],IT[7],IT[0],IT[1],IT[2],IT[3]};
		return swapOutput;
	}
	
	static int[] doFinalPermutation(int IT2[])
	{
		int FP[] = {IT2[3],IT2[0],IT2[2],IT2[4],IT2[6],IT2[1],IT2[7],IT2[5]};
		return FP;
	}
}
