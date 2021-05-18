/**
*
* @author Hikmet Tutuncu   hikmet.tutuncu1@ogr.sakarya.edu.tr
* @since 11.04.2021
* <p>
* 	Bu program Program.cpp dosyasini okuyarak siniflari, siniflarin public
* methodlarini ve parametrelerini, son olarak da hangi siniftan kac kez 
* kalitim alindigini ekrana bastirir. Bir C++ dosyasinin analizi yapilmis olur.
* </p>
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.*;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		try {
			File myObj = new File("./src/Program.cpp");
			Scanner sc = new Scanner(myObj);
			// Okunan tum satirlar bu List icerisinde tutulacak.
			ArrayList<String> satirlar = new ArrayList<String>();
			
			// Satir satir okuma islemi.
			while(sc.hasNext())
				satirlar.add(sc.nextLine());
			
			// Burada analiz edilecek olan Program.cpp dosyasi tek bir string haline getiriliyor.
			// Bunun amaci regex ile istenilen kisimlarin ayristirilabilmesi.
			StringBuilder str = new StringBuilder("");
			for(String s: satirlar) {
				str.append(s);
			}
			
			// Bu string sonradan super siniflarin analizi icin kullanilacak.
			String forSuper = str.toString();
			
			// Bu regex kismi siniflarin ayristirilmasi icin kullaniliyor. 
			// Dosya icerisinde sadece class tanimlamalari icerisindeki kisimlar aliniyor.
			Pattern pattern = Pattern.compile("(?<=class).+?(?=\\};)");
			Matcher matcher = pattern.matcher(str);
			boolean found = true;
			
			// Siniflar bu List icerisinde tutuluyor.
			ArrayList<String> siniflar = new ArrayList<String>();
			while(found) {
				found = matcher.find();
				if(found) {
					siniflar.add(matcher.group().trim());
				}
			}
			
			
			// Tum fonksiyonlar bu list icerisinde tutuluyor. 
			List<String> fonksiyonlar = new ArrayList<String>();
			for(String s: siniflar) {
				// Bu dongu icerisinde adim adim parcalama yapilarak ilgili fonksiyon ve parametreleri ekrana bastiriliyor.
				
				// Sinif ismi parcalanir ve ekrana bastirilir.
				Pattern pp = Pattern.compile("^(\\w+)");// Class adi
				Matcher matchh = pp.matcher(s);
				if(matchh.find()) {
					System.out.println("Sýnýf: " + matchh.group());
				}
				
				// Burada ise sadece public metotlar ayristiriliyor.
				Pattern p = Pattern.compile("(?<=public\\s*:).+");
				Matcher match = p.matcher(s);
				if(match.find()) {
					String str1 = match.group().trim();
					str1 = str1.replaceAll("(?<=\\:).+?(?=[\\{\\}])","");
					str1 = str1.replaceAll("\\{.+?\\}", "");
					str1 = str1.replaceAll("\\}", "");
		        	str1 = str1.replaceAll("const", "");
		        	fonksiyonlar.add(str1);
					// Her fonksiyon listeye atilir.
		        	
		        	// Fonksiyon isimleri bu list icerisinde tutuluyor.
		        	List<String> fonksiyonAdlari = new ArrayList<String>();
		        	
		        	// Burada fonksiyonlarin donus degerleri parcalamasi yapiliyor. Sonuc fonksiyonAdlari listesine atiliyor.
		        	final String regex = "(\\w+&?)?\\s*?(\\w+&?\\*?)?(\\w+<\\w+>\\*?)?\\s*(\\*?\\~?\\w+.?.?)(?=\\()";
					final Pattern pattern1 = Pattern.compile(regex, Pattern.MULTILINE);
			        final Matcher matcher1 = pattern1.matcher(str1);
			        while (matcher1.find()) {
			        	String str2 = matcher1.group(0).replaceAll("\\(", "");
			        	str2 = str2.trim();
			        	fonksiyonAdlari.add(str2);
			        }
			        
			        // Burada fonksiyon parametrelerinin ayristirilmasi yapiliyor ve ilgili listeye atiliyor.
			        List<String> parametreler = new ArrayList<String>();
			        final Pattern pattern1para = Pattern.compile("(?<=\\()(.*?)(?=\\))", Pattern.MULTILINE);
			        final Matcher matcher1para = pattern1para.matcher(str1);
			        
			        while (matcher1para.find()) {
			            parametreler.add(matcher1para.group(0));			            
			        }
			        
			        // Listeler doldurulduktan sonra fonksiyonlarin donus turleri ve parametrelerinin ekrana basilmasi burada yapiliyor.
			        
			        for(int i=0; i<fonksiyonAdlari.size(); i++) {
			        	
			        	// Donus turu ve fonksiyon adi burada olusturuluyor.
			        	String donusTuru;
			        	String fonksiyonAdi;
			        	if(fonksiyonAdlari.get(i).split(" ").length == 1) {
			        		donusTuru = "Nesne Adresi";
			        		fonksiyonAdi = fonksiyonAdlari.get(i);
			        	}else if(fonksiyonAdlari.get(i).split(" ").length == 2) {
			        		donusTuru = fonksiyonAdlari.get(i).split(" ")[0].trim();
			        		fonksiyonAdi = fonksiyonAdlari.get(i).split(" ")[1].trim();
			        	}else {
			        		donusTuru = fonksiyonAdlari.get(i).split(" ")[1].trim();
			        		fonksiyonAdi = fonksiyonAdlari.get(i).split(" ")[2].trim();
			        	}
			        	
			        	System.out.println("\t" + fonksiyonAdi);
			        	
			        	// Parametrelerin sadece veri tipleri ekrana basiliyor.
			        	if( parametreler.get(i).length() != 0 ) {
			        		String[] param = {""};
			        		param = parametreler.get(i).split(",");
			        		
			        		
			        		System.out.print("\t\tParametre: "+param.length+" (");
				        	for(int j=0; j<param.length; j++) {
				        		
				        		if(param[j].split(" ").length == 2)
				        			param[j] = param[j].split(" ")[0].trim();
				        		else if(param[j].split(" ").length == 3)
				        			param[j] = param[j].split(" ")[1].trim();
				        		
				        		System.out.print(param[j]);
				        		if( j != param.length - 1)
				        			System.out.print(",");
				        	}
				        	System.out.println(")");
			        	}
			        	else {
			        		System.out.println("\t\tParametre: "+ "0");
			        	}
			        	
			        	System.out.println("\t\tDönüþ Türü: "+donusTuru);
			        }	
				}
			}
			
			// Dongu bittikten sonra kalitim alinan siniflarin analizi icin dosya taraniyor.
			final String regex = "(?<=class)\\s+\\w+\\s?:\\s+?(public|private)\\s+(\\w+\\s*?)(?=\\{)";
			final Pattern pattern1 = Pattern.compile(regex, Pattern.MULTILINE);
	        final Matcher matcher1 = pattern1.matcher(forSuper);
	        
	        System.out.println("Super Sýnýflar:");
	        
	        // Bu listede ayristirilan super siniflar tutuluyor.
	        List<String> superSiniflar = new ArrayList<String>();
	        while (matcher1.find()) {
	        	superSiniflar.add(matcher1.group(2).trim());
	        }
	        
	        // Set olusturulup super siniflar listesi bunun icerisine atiliyor.
	        // Bunu yazmamin sebebi set veri yapisi tekrar eden verileri siliyor.
	        // Boylece elimde tekrar etmeyen bir liste oluyor.
	        Set<String> set = new HashSet<String>(superSiniflar);
	        
	        // Tekrar etmeyen liste icerisinde dongu ile donerek her adimda super siniflar listesi taraniyor.
	        // Ekrana kac adet bulundugu bastiriliyor.
	        for(String s : set) {
	        	System.out.println("\t" + s + ": "+ Collections.frequency(superSiniflar, s));	        	
	        }
	        
	        sc.close();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}

}
