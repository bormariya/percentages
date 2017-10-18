package calc;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class Main {
	
private static JPanel panForPrice;

private static JTextField txtUSD;
private static JTextField txtEUR;
private static JTextField txtOurPrice;
private static JTextField txtDiscount;
private static JTextField txtPriceWithDiscount;
private static JTextField txtOurPrice2;
private static JTextField txtConqPrice;

private static JButton calculatePrice;
private static JButton calculateDifference;

private static DefaultTableModel tblModel;
private static JTable tbl;

private static JLabel lblItog;

private static JPanel mainPanel;
private static JPanel panCurr;
private static JPanel panData;
private static JPanel panItog1;
private static JPanel panItog2;

public static void main(String[] args) {
	SimpleFrame jfr = new SimpleFrame();
	jfr.setContentPane(getMainPanel());
	jfr.setVisible(true);
}

//private static JPanel getFirstPanel(){
//	panForPrice = new JPanel();
//	panForPrice.setLayout(new MigLayout("wrap", "[fill,grow]", null));
//		
//	return panForPrice;
//}

private static Double getPriceWithDiscount(Double ourPrice, Double dscnt){
	Double withDiscount = ourPrice - (ourPrice*dscnt/100);
	return withDiscount;
}

private static void doActionCalculatePrice(){

	Double dblOurPrice = new BigDecimal(Double.valueOf(txtOurPrice.getText().replace(",", "."))).setScale(2).doubleValue();
	Double dblDiscount = new BigDecimal(Double.valueOf(txtDiscount.getText().replace(",", "."))).setScale(2).doubleValue();
	txtPriceWithDiscount.setText(getPriceWithDiscount(dblOurPrice, dblDiscount).toString());
	Double dblPriceWithDiscount = Double.valueOf(txtPriceWithDiscount.getText().replace(",", "."));
	Double dblPriceWithDiscountUSD = new BigDecimal(dblPriceWithDiscount/(Double.valueOf(txtUSD.getText().replace(",", ".")))).setScale(2, 1).doubleValue();
	Double dblPriceWithDiscountEUR = new BigDecimal(dblPriceWithDiscount/(Double.valueOf(txtEUR.getText().replace(",", ".")))).setScale(2, 1).doubleValue();
	
	Double difference = dblOurPrice - dblPriceWithDiscount;
	Double differenceUSD = new BigDecimal(difference/(Double.valueOf(txtUSD.getText().replace(",", ".")))).setScale(2, 1).doubleValue();
	Double differenceEUR = new BigDecimal(difference/(Double.valueOf(txtEUR.getText().replace(",", ".")))).setScale(2, 1).doubleValue();
	
	String[] values = new String[] {dblOurPrice.toString(), dblDiscount.toString(), 
			dblPriceWithDiscount.toString()+" | "+dblPriceWithDiscountUSD.toString()+" | "+dblPriceWithDiscountEUR.toString(),
			difference.toString()+" | "+differenceUSD.toString()+" | "+differenceEUR.toString()};

		tblModel.addRow(values);
}

public static void doActionCalculateDifference(){
	Double ourPrice = new Double(txtOurPrice2.getText().replace(",", "."));
	Double conqPrice = new Double(txtConqPrice.getText().replace(",", "."));
	
	BigDecimal ourPerc = new BigDecimal((ourPrice*100)/(conqPrice)).setScale(2, 1);
	BigDecimal difference = (new BigDecimal(100)).subtract(ourPerc);
	
	String itog = "";
	if (difference.compareTo(new BigDecimal(0)) > 0)
		itog = "Предлагаемая цена ниже цены конкурентов на " + difference + "% (или на " + (conqPrice-ourPrice) + " руб.)";
	else if (difference.compareTo(new BigDecimal(0)) < 0)
		itog = "Предлагаемая цена выше цены конкурентов на " + difference.multiply(new BigDecimal(-1)) + 
							"% (или на " + (conqPrice-ourPrice)*(-1) + " руб.)";
	else
		itog = "Цены эквивалентны";
	
	lblItog.setText(itog);
}

private static JPanel getMainPanel(){
	if(mainPanel == null){
		mainPanel = new JPanel();
		mainPanel.setLayout(new MigLayout("wrap, ins 1", "[fill,grow]", null));
		
		mainPanel.add(getPanCurr(), "span, growx");
		mainPanel.add(getPanData(), "span, growx");
		
		calculatePrice = new JButton();
		calculatePrice.setText("Посчитать цену");
		calculatePrice.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				doActionCalculatePrice();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});	
		mainPanel.add(calculatePrice, "wrap");
		
		mainPanel.add(getPanItog1(), "wrap");
		
		final Object[] header = new String[] {"Исходная цена, руб", "Скидка, %", "Цена со скидкой (RUB | USD | EUR)", "Разница (RUB | USD | EUR)"};
		tblModel = new DefaultTableModel(header, 0);
		tbl = new JTable(tblModel);
		mainPanel.add(new JScrollPane(tbl), "wrap");
		
		mainPanel.add(getPanItog2(), "wrap");
	}
	return mainPanel;
}

private static JPanel getPanCurr(){
	if(panCurr == null){
		panCurr = new JPanel();
		panCurr.setLayout(new MigLayout("wrap", "[r][fill,grow][r][fill,grow]"));
		panCurr.setBorder(new TitledBorder("Введите курсы валют"));

		panCurr.add(new JLabel("USD = "));
		txtUSD = new JTextField();
		panCurr.add(txtUSD);
		
		panCurr.add(new JLabel("EUR = "));
		txtEUR = new JTextField();
		panCurr.add(txtEUR);
	}
	return panCurr;
}

private static JPanel getPanData(){
	if(panData == null){
		panData = new JPanel();
		panData.setLayout(new MigLayout("wrap", "[r][fill,grow][r][fill,grow]"));
		panData.setBorder(new TitledBorder("Исходные данные"));
		
		panData.add(new JLabel("Исходная цена, руб"));
		txtOurPrice = new JTextField();
		panData.add(txtOurPrice);
		
		panData.add(new JLabel("Скидка, %"));
		txtDiscount = new JTextField();
		panData.add(txtDiscount);
		
	}
	return panData;
}

private static JPanel getPanItog1(){
	if(panItog1 == null){
		panItog1 = new JPanel();
		panItog1.setLayout(new MigLayout("wrap", "[r][fill,grow][r][fill,grow]"));
		panItog1.setBorder(new TitledBorder("Итог"));
		
		txtPriceWithDiscount = new JTextField();
		panItog1.add(new JLabel("Цена со скидкой, руб."));
		panItog1.add(txtPriceWithDiscount);
	}
	return panItog1;
}

private static JPanel getPanItog2(){
	if(panItog2 == null){
		panItog2 = new JPanel();
		panItog2.setLayout(new MigLayout("wrap","[][fill,grow]"));
		panItog2.setBorder(new TitledBorder("Сравнение цен"));
		
		panItog2.add(new JLabel("Предлагаемая цена, руб."));
		txtOurPrice2 = new JTextField();
		panItog2.add(txtOurPrice2);
		
		panItog2.add(new JLabel("Цена конкурентов, руб."));
		txtConqPrice = new JTextField();
		panItog2.add(txtConqPrice);
		
		calculateDifference = new JButton();
		calculateDifference.setText("Посчитать выгоду/потери");
		panItog2.add(calculateDifference);
		
		calculateDifference.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				doActionCalculateDifference();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});	
		
		lblItog = new JLabel("Здесь выводится результат сравнения цен");
		Font fontLabelItog = new Font("Verdana", Font.BOLD, 13);
		lblItog.setFont(fontLabelItog);
		lblItog.setHorizontalAlignment(JLabel.CENTER);
		lblItog.setForeground(Color.RED);
		panItog2.add(lblItog);
	}
	return panItog2;
}

}

