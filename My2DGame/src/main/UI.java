package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font NanumSquareE , NanumSquareOTF;
	BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
	public boolean messageOn = false;
//	public String message = "";
//	int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDialogue ="";
	public int commandNum = 0;
	public int titleScreenState = 0; // 0 : the first screen
	public int playerSlotCol = 0;
	public int playerSlotRow = 0;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;
	int subState = 0;
	int counter = 0;
	public Entity npc;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/NanumSquareEB.ttf");
			NanumSquareE = Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/NanumSquareOTF_acB.otf");
			NanumSquareOTF = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
		Entity crystal = new OBJ_ManaCrystal(gp);
		crystal_full = crystal.image;
		crystal_blank = crystal.image2;
		Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
		coin = bronzeCoin.down1;
		
	}
	public void addMessae(String text) {
		message.add(text);
		messageCounter.add(0);
	}
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		g2.setFont(NanumSquareE);
		g2.setColor(Color.white);
		
		// TITLE STATE
		if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		// PLAY STATE
		if(gp.gameState == gp.playState) {
			drawPlayerLife();
			drawMessage();
		}
		// PAUSE STATE
		if(gp.gameState == gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		// DIALOGUE STATE
		if(gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
		}
		// CHARACTER STATE
		if(gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawInventory(gp.player,true);
		}
		// OPTIONS STATE
		if(gp.gameState == gp.optionState) {
			drawOptionScreen();
		}
		// GAMEOVER STATE
		if(gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		}
		// TRANSITION STATE
		if(gp.gameState == gp.transitionState) {
			drawTransition();
		}
		// TRADE STATE
		if(gp.gameState == gp.tradeState) {
			drawTradeScreen();
		}
	}
	public void drawPlayerLife() {
		
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		
		// DRAW MAX HEART
		while (i<gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		
		// RESET
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		// DRAW CURRENT LIFE
		while (i < gp.player.life) {
			g2.drawImage(heart_half, x, y,null);
			i++;
			if(i<gp.player.life) {
				g2.drawImage(heart_full, x, y,null);
			}
			i++;
			x += gp.tileSize;
		}
		
		//DRAW MAX MANA
		x = (gp.tileSize/2)-5;
		y = (int) (gp.tileSize*1.5);
		i = 0;
		
		while (i < gp.player.maxMana) {
			g2.drawImage(crystal_blank, x, y,null);
			i++;
			x += 35;
		}
		// DRAW MANA
		x = (gp.tileSize/2)-5;
		y = (int) (gp.tileSize*1.5);
		i = 0;
		while (i < gp.player.mana) {
			g2.drawImage(crystal_full, x, y,null);
			i++;
			x+=35;
			
		}
	}
	private void drawMessage() {

		int messageX = gp.tileSize;
		int messageY = gp.tileSize*4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
		
		for(int i = 0; i < message.size(); i++) {
			if(message.get(i) != null) {
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) +1; // messageCounter++
				messageCounter.set(i, counter); // set the counter to the array
				messageY += 50;
				
				if(messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	public void drawTitleScreen() {
		if(titleScreenState ==0) {
			g2.setColor(new Color(0,0,0));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			//TITLE NAME
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
			String text = "진경이의 모험";
			int x = getXforCenteredText(text);
			int y = gp.tileSize * 3;
			
			// SHADOW
			g2.setColor(Color.gray);
			g2.drawString(text, x+5, y+5);
			//MAIN COLOR
			g2.setColor(Color.white);
			g2.drawString(text, x, y);
			
			// 진경 iMAGE
			x = gp.screenWidth/2 -(gp.tileSize*2)/2;
			y += gp.tileSize*2;
			UtilityTool uTool = new UtilityTool();
			BufferedImage jin = null;
			
			try {
				jin = ImageIO.read(getClass().getResourceAsStream("/objects/jin.jpg"));
				jin = uTool.scaleImage(jin, gp.tileSize, gp.tileSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
			g2.drawImage(jin, x, y,gp.tileSize*2,gp.tileSize*2,null);
			try {
				jin = ImageIO.read(getClass().getResourceAsStream("/objects/jin2.jpg"));
				jin = uTool.scaleImage(jin, gp.tileSize, gp.tileSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
			g2.drawImage(jin, x-300, y+50,gp.tileSize*2,gp.tileSize*4,null);
			try {
				jin = ImageIO.read(getClass().getResourceAsStream("/objects/jin3.jpg"));
				jin = uTool.scaleImage(jin, gp.tileSize, gp.tileSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
			g2.drawImage(jin, x+300, y+2,gp.tileSize*2,gp.tileSize*2,null);
			//MENU
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
			
			text = "NEW GAME";
			x = getXforCenteredText(text);
			y+=gp.tileSize*3.5;
			g2.drawString(text, x, y);
			if(commandNum == 0) {
				g2.drawString(">", x-gp.tileSize, y);
			}
			text = "LOAD GAME";
			x = getXforCenteredText(text);
			y+=gp.tileSize;
			g2.drawString(text, x, y);
			if(commandNum == 1) {
				g2.drawString(">", x-gp.tileSize, y);
			}
			text = "QUIT";
			x = getXforCenteredText(text);
			y+=gp.tileSize;
			g2.drawString(text, x, y);
			if(commandNum == 2) {
				g2.drawString(">", x-gp.tileSize, y);
			}
		}
//		else if(titleScreenState == 1) {
//			
//			// CLASS SELECTION SCREEN
//			g2.setColor(Color.white);
//			g2.setFont(g2.getFont().deriveFont(42F));
//			
//			String text = "Select your class!";
//			int x = getXforCenteredText(text);
//			int y = gp.tileSize * 3;
//			g2.drawString(text, x, y);
//			
//			text = "Fighter";
//			x = getXforCenteredText(text);
//			y += gp.tileSize * 3;
//			g2.drawString(text, x, y);
//			if(commandNum == 0) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//			
//			text = "Thief";
//			x = getXforCenteredText(text);
//			y += gp.tileSize;
//			g2.drawString(text, x, y);
//			if(commandNum == 1) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//			
//			text = "Sorcerer";
//			x = getXforCenteredText(text);
//			y += gp.tileSize;
//			g2.drawString(text, x, y);
//			if(commandNum == 2) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//			text = "Back";
//			x = getXforCenteredText(text);
//			y += gp.tileSize * 2;
//			g2.drawString(text, x, y);
//			if(commandNum == 3) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//		}
	}
	public void drawDialogueScreen() {
		// WINDOW
		int x = gp.tileSize*3;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*6);
		int height = gp.tileSize*4;
		
		drawSubWindow(x, y, width, height);
		
		g2.setFont(new Font("나늠스퀘어", Font.PLAIN, 27));
		x+=gp.tileSize;
		y+=gp.tileSize;
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}
	public void drawCharacterScreen() {
		
		// CREATE A FRAME
		final int frameX = gp.tileSize*2;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 5;
		final int frameHeight = gp.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		// NAMES
		g2.drawString("Level", textX, textY); textY += lineHeight;
		g2.drawString("Life", textX, textY); textY += lineHeight;
		g2.drawString("Mana", textX, textY); textY += lineHeight;
		g2.drawString("Strength", textX, textY); textY += lineHeight;
		g2.drawString("Dexterity", textX, textY); textY += lineHeight;
		g2.drawString("Attack", textX, textY); textY += lineHeight;
		g2.drawString("Defecne", textX, textY); textY += lineHeight;
		g2.drawString("Exp", textX, textY); textY += lineHeight;
		g2.drawString("Next Level", textX, textY); textY += lineHeight;
		g2.drawString("Coin", textX, textY); textY += lineHeight + 20;
		g2.drawString("Weapon", textX, textY); textY += lineHeight + 15;
		g2.drawString("Shield", textX, textY); textY += lineHeight;
		
		// VALUES
		int tailX = (frameX + frameWidth) - 30;
		// Reset TextY
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.strength);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.attack);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.defense);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.exp);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.coin);
		textX = getXforAlightToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-24, null);
		textY += gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-24, null);

		
	}
	public void drawInventory(Entity entity, boolean cursor) {
		
		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;
		
		if(entity == gp.player) {
			frameX = gp.tileSize*12;
			frameY = gp.tileSize;
			frameWidth =gp.tileSize*6;
			frameHeight = gp.tileSize*5;
			slotCol = playerSlotCol;
			slotRow = playerSlotRow;
		}
		else {
			frameX = gp.tileSize*2;
			frameY = gp.tileSize;
			frameWidth =gp.tileSize*6;
			frameHeight = gp.tileSize*5;
			slotCol = npcSlotCol;
			slotRow = npcSlotRow;
		}
		// FRAME
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// SLOT
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gp.tileSize+3;
		
		// DRAW PLAYER'S ITEMS
		for(int i = 0; i < entity.inventory.size(); i++) {
			
			// EQUIP CURSUR
			if(entity.inventory.get(i) == entity.currentWeapon ||
					entity.inventory.get(i) == entity.currentShield) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
				
			}
			
			g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
			
			slotX += slotSize;
			
			if(i == 4 || i == 9 || i== 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
		}
		
		// CURSOR
		if(cursor ==true) {
			int cursorX = slotXstart + (slotSize * slotCol);
			int cursorY = slotYstart + (slotSize * slotRow);
			int cursorWidth = gp.tileSize;
			int cursorHeight = gp.tileSize;
			// DRAW CURSOR
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
			
			// DESCRIPTION FRAME
			int dFrameX = frameX;
			int dFrameY = frameY + frameHeight;
			int dFreamWidth = frameWidth;
			int dFrameHeight = gp.tileSize*3;
			// DRAW DESCRIPTION TEXT
			int textX = dFrameX +20;
			int textY = dFrameY + gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(28F));
			
			int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
			
			if(itemIndex < entity.inventory.size()) {
				drawSubWindow(dFrameX, dFrameY, dFreamWidth, dFrameHeight);
				for(String line: entity.inventory.get(itemIndex).description.split("\n")) {
					g2.drawString(line, textX, textY);
					textY += 32;
				}
			}
		}
	}
	public void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x; 
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
		
		text = "게임 오바~";
		// Shadow
		g2.setColor(Color.black);
		x = getXforCenteredText(text);
		y = gp.tileSize*4;
		g2.drawString(text, x, y);
		// Main
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		// Retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "재도전";
		x = getXforCenteredText(text);
		y+= gp.tileSize*4;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-40, y);
		}
		
		// Back to the title screen
		text = "Quit";
		x = getXforCenteredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-40, y);
		}
	}
	public void drawOptionScreen() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		// SUB WINDOW
		int frameX = gp.tileSize*6;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize*8;
		int frameHeight = gp.tileSize*10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		switch(subState) {
		case 0: options_top(frameX,frameY); break;
		case 1: options_fullScreenNotification(frameX, frameY);break;
		case 2: options_control(frameX, frameY);break;
		case 3: options_endGameConfirmation(frameX, frameY);break;
		}
		
		gp.keyH.enterPressed = false;
	}
	public void options_top(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		//TITLE
		String text = "설정";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		// FULL SCREEN ON/OFF
		textX = frameX + gp.tileSize;
		textY += gp.tileSize*2;
		g2.drawString("전체 화면", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed==true) {
				if(gp.fullScreenOn == false) {
					gp.fullScreenOn = true;
				}
				else if(gp.fullScreenOn == true) {
					gp.fullScreenOn = false;
				}
				subState = 1;
			}
		}
		
		// MUSIC
		textY += gp.tileSize;
		g2.drawString("음악", textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX-25, textY);
		}
		// SE
		textY += gp.tileSize;
		g2.drawString("음향효과", textX, textY);
		if(commandNum == 2) {
			g2.drawString(">", textX-25, textY);
		}
		//CONTROL
		textY += gp.tileSize;
		g2.drawString("키 알아보기", textX, textY);
		if(commandNum == 3) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 2;
				commandNum= 0;
			}
		}
		// END GAME
		textY += gp.tileSize;
		g2.drawString("게임 종료", textX, textY);
		if(commandNum == 4) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 3;
				commandNum = 0;
			}
		}
		// BACK
		textY += gp.tileSize*2;
		g2.drawString("뒤로", textX, textY);
		if(commandNum == 5) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				gp.gameState = gp.playState;
				commandNum = 0;
			}
		}
		
		// FULL SCREEN CHECK BOX
		textX = frameX + (int)(gp.tileSize*4.5);
		textY = frameY + gp.tileSize*2 +24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, 24, 24);
		if(gp.fullScreenOn == true) {
			g2.fillRect(textX, textY, 24, 24);
		}
		
		// MUSIC VOLUME
		textY += gp.tileSize;
		g2.drawRect(textX, textY,120,24);
		int volumeWidth = 24 * gp.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		// SE VOLUME
		textY += gp.tileSize;
		g2.drawRect(textX, textY,120,24);
		volumeWidth = 24 * gp.se.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		gp.config.saveConfig();
		
	}
	public void options_fullScreenNotification(int frameX, int frameY) {
		
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize*3;
		
		currentDialogue = "게임을 다시 시작하면 \n바뀝니다용~";
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
		
		// BACK
		textY = frameY + gp.tileSize *9;
		g2.drawString("뒤로", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed ==true)
				subState = 0;
		}
	}
	public void options_control(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		// TITLE
		String text = "키 알아보기";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		textX = frameX + gp.tileSize;
		textY += gp.tileSize;
		g2.drawString("이동", textX, textY); textY+=gp.tileSize;
		g2.drawString("확인/공격", textX, textY); textY+=gp.tileSize;
		g2.drawString("발사/캐스트", textX, textY); textY+=gp.tileSize;
		g2.drawString("캐릭터 화면", textX, textY); textY+=gp.tileSize;
		g2.drawString("일시 정지", textX, textY); textY+=gp.tileSize;
		g2.drawString("옵션", textX, textY); textY+=gp.tileSize;
		
		textX = frameX + gp.tileSize*5;
		textY = frameY + gp.tileSize*2;
		g2.drawString("WASD", textX, textY); textY+=gp.tileSize;
		g2.drawString("ENTER", textX, textY); textY+=gp.tileSize;
		g2.drawString("F", textX, textY); textY+=gp.tileSize;
		g2.drawString("C", textX, textY); textY+=gp.tileSize;
		g2.drawString("P", textX, textY); textY+=gp.tileSize;
		g2.drawString("ESC", textX, textY); textY+=gp.tileSize;
		
		// BACK
		textX = frameX + gp.tileSize;
		textY = frameY + gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 3;
			}
		}
	}
	public void options_endGameConfirmation(int frameX,int frameY) {
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize*3;
		
		currentDialogue = "정말 게임을 \n종료 하시겠습니까?";
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY +=40;
		}
		
		// YES
		String text ="네";
		textX = getXforCenteredText(text);
		textY += gp.tileSize*3;
		g2.drawString(text, textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
				gp.gameState =gp.titleState;
			}
		}
		// NO
		text ="아니요";
		textX = getXforCenteredText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 4;
			}
		}
	}
	public void drawTransition() {
		
		counter++;
		g2.setColor(new Color(0,0,0,counter*5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		if(counter == 50) {
			counter = 0;
			gp.gameState = gp.playState;
			gp.currentMap = gp.eHandler.tempMap;
			gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
			gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
			gp.eHandler.previousEventX = gp.player.worldX;
			gp.eHandler.previousEventY = gp.player.worldY;
		}
	}
	public void drawTradeScreen(){
		switch (subState) {
		case 0: trade_select(); break;
		case 1: trade_buy(); break;
		case 2: trade_sell(); break;
		}
		gp.keyH.enterPressed = false;
	}
	public void trade_select() {
		drawDialogueScreen();
		
		// DRAW WINDOW
		int x = gp.tileSize * 15;
		int y = gp.tileSize * 4;
		int width = gp.tileSize * 3;
		int height = (int)(gp.tileSize * 3.5);
		drawSubWindow(x, y, width, height);
		
		// DRAW TEXTS
		x += gp.tileSize;
		y += gp.tileSize;
		g2.drawString("구매", x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-24, y);
			if(gp.keyH.enterPressed == true) {
				subState =1;
			}
		}
		y += gp.tileSize;
		g2.drawString("판매", x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-24, y);
			if(gp.keyH.enterPressed == true) {
				subState =2;
			}
		}
		y += gp.tileSize;
		g2.drawString("떠나기", x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-24, y);
			if(gp.keyH.enterPressed == true) {
				commandNum =0;
				gp.gameState = gp.dialogueState;
				currentDialogue = "다시 오게나 히히!";
			}
		}
		y += gp.tileSize;
	}
	public void trade_buy() {
			
		// DRAW PLAYER INVENTORY
		drawInventory(gp.player, false);
		// DRAW NPC INVENTORY
		drawInventory(npc, true);
		
		// DRAW HINT WINDOW
		int x = gp.tileSize*2;
		int y = gp.tileSize*9;
		int width = gp.tileSize*6;
		int height = gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[ESC] 뒤로가기", x+24, y+60);
		
		// DRAW PLAYER COIN WINDOW
		x = gp.tileSize*12;
		y = gp.tileSize*9;
		width = gp.tileSize*6;
		height = gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("코인 수: " + gp.player.coin, x+24, y+60);
		
		// DRAW PRICE WINDOW
		int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
		if(itemIndex < npc.inventory.size()) {
			
			x = (int)(gp.tileSize*5.5);
			y = (int)(gp.tileSize*5.5);
			width = (int)(gp.tileSize*2.5);
			height = gp.tileSize;
			drawSubWindow(x, y, width, height);
			g2.drawImage(coin, x+10, y+10, 32, 32, null);
			
			int price = npc.inventory.get(itemIndex).price;
			String text = ""+price;
			x = getXforAlightToRightText(text, gp.tileSize*8-20);
			g2.drawString(text, x, y+34);
			
			// BUY AN ITEM
			if(gp.keyH.enterPressed == true) {
				if(npc.inventory.get(itemIndex).price> gp.player.coin) {
					subState=0;
					gp.gameState = gp.dialogueState;
					currentDialogue = "이걸 사려면 돈이 더 필요해!";
					drawDialogueScreen();
				}
				else if(gp.player.inventory.size() == gp.player.maxIventorySize) {
					subState = 0;
					gp.gameState = gp.dialogueState;
					currentDialogue = "더 들 수 가 없단다!";
				}
				else {
					gp.player.coin -= npc.inventory.get(itemIndex).price;
					gp.player.inventory.add(npc.inventory.get(itemIndex));
				}
			}
		}
	}
	public void trade_sell() {
		
		// DRAW PLAYER INVENTROY
		drawInventory(gp.player, true);
		
		int x;
		int y;
		int width;
		int height;
		
		// DRAW HINT WINDOW
				x = gp.tileSize*2;
				y = gp.tileSize*9;
				width = gp.tileSize*6;
				height = gp.tileSize*2;
				drawSubWindow(x, y, width, height);
				g2.drawString("[ESC] 뒤로가기", x+24, y+60);
				
				// DRAW PLAYER COIN WINDOW
				x = gp.tileSize*12;
				y = gp.tileSize*9;
				width = gp.tileSize*6;
				height = gp.tileSize*2;
				drawSubWindow(x, y, width, height);
				g2.drawString("코인 수: " + gp.player.coin, x+24, y+60);
				
				// DRAW PRICE WINDOW
				int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
				if(itemIndex < gp.player.inventory.size()) {
					
					x = (int)(gp.tileSize*15.5);
					y = (int)(gp.tileSize*5.5);
					width = (int)(gp.tileSize*2.5);
					height = gp.tileSize;
					drawSubWindow(x, y, width, height);
					g2.drawImage(coin, x+10, y+10, 32, 32, null);
					
					int price = gp.player.inventory.get(itemIndex).price/2;
					String text = ""+price;
					x = getXforAlightToRightText(text, gp.tileSize*18-20);
					g2.drawString(text, x, y+34);
					
					// SELL AN ITEM
					if(gp.keyH.enterPressed == true) {
						if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || 
								gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
							commandNum = 0;
							subState = 0;
							gp.gameState = gp.dialogueState;
							currentDialogue = "너는 끼고 있는 아이템은 팔수없엉ㅇㅇㅇ";
						}
						else {
							gp.player.inventory.remove(itemIndex);
							gp.player.coin += price;
						}
					}
				}
	}
	public int getItemIndexOnSlot(int slotCol, int slotRow) {
		int itemIndex = slotCol + (slotRow*5);
		return itemIndex;
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0,0,0,170);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c= new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text = "PAUSED";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	
	public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	public int getXforAlightToRightText(String text,int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
}