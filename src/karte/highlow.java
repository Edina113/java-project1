package karte;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

/**
 * Ovaj program je jednostavna igra karti. Korisnik vidi kartu
 * i pokušava pogoditi da li je sljedeæa karta veæa ili manja.
 * As je karta najniže vrijednosti. Ako korisnik pogodi tri puta
 * pobijedio je, u suprotnom izgubio je.
 * 
 * Ova klasa definira panel, ali i sadrži main() koja omoguæuje 
 * pokretanje programa kao samostalne aplikacije.
 * 
 * Program ovisi i o nekoliko dodatnih datoteka izvornog koda:
 * Card.java, Hand.java, i Deck.java. Takoðer zahtijeva i datoteku 
 * u kojoj se nalaze sve slike karti.
 */
public class highlow extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
      JFrame window = new JFrame("Igra veæe/manje");
      highlow content = new highlow();
      window.setContentPane(content);
      window.pack();  // Postavi velièinu prozora tako da stane cijeli sadržajSet size of window to preferred size of its contents.
      window.setResizable(false);  // Korisnik ne može promijeniti velièinu prozora
      window.setLocation(300,200);
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setVisible(true);
   }
   
      
   /**
    * The constructor lays out the panel.  A CardPanel occupies the CENTER 
    * position of the panel (where CardPanel is a subclass of JPanel that is 
    * defined below).  On the bottom is a panel that holds three buttons.  
    * The CardPanel listens for ActionEvents from the buttons and does all 
    * the real work of the program.
    */
   public highlow() {
      
      setBackground( new Color(130,50,40) );
      
      setLayout( new BorderLayout(3,3) );
      
      CardPanel board = new CardPanel();
      add(board, BorderLayout.CENTER);
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBackground( new Color(220,200,180) );
      add(buttonPanel, BorderLayout.SOUTH);
      
      JButton higher = new JButton( " Veæa " );
      higher.addActionListener(board);
      buttonPanel.add(higher);
      
      JButton lower = new JButton( " Manja " );
      lower.addActionListener(board);
      buttonPanel.add(lower);
      
      JButton same = new JButton( " Ista " );
      same.addActionListener(board);
      buttonPanel.add(same);
      
      JButton newGame = new JButton( " Nova Igra " );
      newGame.addActionListener(board);
      buttonPanel.add(newGame);
      
      setBorder(BorderFactory.createLineBorder( new Color(130,50,40), 3) );
      
   }  // end constructor
   
   
   
   /**
    * A nested class that displays the cards and does all the work
    * of keeping track of the state and responding to user events.
    */
   private class CardPanel extends JPanel implements ActionListener {
      
	private static final long serialVersionUID = 1L;
	Deck deck;       // A deck of cards to be used in the game.
      Hand hand;       // The cards that have been dealt.
      String message;  // A message drawn on the canvas, which changes
                       //    to reflect the state of the game.
      
      boolean gameInProgress;  // Set to true when a game begins and to false
                               //   when the game ends.
      
      Font bigFont;      // Font that will be used to display the message.
      
      Image cardImages;  // Contains the image of all 52 cards 
      
      /**
       * Constructor creates fonts, sets the foreground and background
       * colors and starts the first game.  It also sets a "preferred
       * size" for the panel.  This size is respected when the program
       * is run as an application, since the pack() method is used to
       * set the size of the window.
       */
      CardPanel() {
         loadImage();
         setBackground( new Color(0,120,0) );
         setForeground( Color.GREEN );
         bigFont = new Font("Serif", Font.BOLD, 15);
         setPreferredSize( new Dimension(15+4*(15+79), 185));
         doNewGame();
      } // end constructor
      
      
      /**
       * Respond when the user clicks on a button by calling the appropriate 
       * method.  Note that the buttons are created and listening is set
       * up in the constructor of the HighLowPanel class.
       */
      public void actionPerformed(ActionEvent evt) {
         String command = evt.getActionCommand();
         if (command.equals(" Veæa "))
            doHigher();
         else if (command.equals(" Manja "))
            doLower();
         else if (command.equals(" Ista "))
             doSame();
         else if (command.equals(" Nova Igra "))
            doNewGame();
      } // end actionPerformed()
      
      
      /**
       * Called by actionPerformmed() when user clicks "Higher" button.
       * Check the user's prediction.  Game ends if user guessed
       * wrong or if the user has made three correct predictions.
       */
      void doHigher() {
         if (gameInProgress == false) {
            // If the game has ended, it was an error to click "Higher",
            // So set up an error message and abort processing.
            message = "Kliknite \"Nova Igra\" za poèetak nove igre!";
            Toolkit.getDefaultToolkit().beep();
            repaint();
            return;
         }
         hand.addCard( deck.dealCard() );     // Deal a card to the hand.
         int cardCt = hand.getCardCount();
         Card thisCard = hand.getCard( cardCt - 1 );  // Card just dealt.
         Card prevCard = hand.getCard( cardCt - 2 );  // The previous card.
         if ( thisCard.getValue() < prevCard.getValue() ) {
            gameInProgress = false;
            message = "Izgubili ste! ";
         }
         else if ( thisCard.getValue() == prevCard.getValue() ) {
            gameInProgress = false;
            message = "Izgubili ste!";
         }
         else if ( cardCt == 4) {
            gameInProgress = false;
            message = "Pobijedili ste! Pogodili ste tri puta.";
         }
         else {
            message = "Odlièno! Pokušaj " + cardCt + ".";
         }
         repaint();
      } // end doHigher()
      
      
      /**
       * Called by actionPerformmed() when user clicks "Lower" button.
       * Check the user's prediction.  Game ends if user guessed
       * wrong or if the user has made three correct predictions.
       */
      void doLower() {
         if (gameInProgress == false) {
               // If the game has ended, it was an error to click "Lower",
               // So set up an error message and abort processing.
            message = "Kliknite \"Nova Igra\" za poèetak nove igre!";
            Toolkit.getDefaultToolkit().beep();
            repaint();
            return;
         }
         hand.addCard( deck.dealCard() );     // Deal a card to the hand.
         int cardCt = hand.getCardCount();
         Card thisCard = hand.getCard( cardCt - 1 );  // Card just dealt.
         Card prevCard = hand.getCard( cardCt - 2 );  // The previous card.
         if ( thisCard.getValue() > prevCard.getValue() ) {
            gameInProgress = false;
            message = "Izgubili ste!";
         }
         else if ( thisCard.getValue() == prevCard.getValue() ) {
            gameInProgress = false;
            message = "Izgubili ste!";
         }
         else if ( cardCt == 4) {
            gameInProgress = false;
            message = "Pobijedili ste! Pogodili ste tri puta.";
         }
         else {
            message = "Odlièno! Pokušaj " + cardCt + ".";
         }
         repaint();
      } // end doLower()
      
      void doSame() {
          if (gameInProgress == false) {
                // If the game has ended, it was an error to click "Lower",
                // So set up an error message and abort processing.
             message = "Kliknite \"Nova Igra\" za poèetak nove igre!";
             Toolkit.getDefaultToolkit().beep();
             repaint();
             return;
          }
          hand.addCard( deck.dealCard() );     // Deal a card to the hand.
          int cardCt = hand.getCardCount();
          Card thisCard = hand.getCard( cardCt - 1 );  // Card just dealt.
          Card prevCard = hand.getCard( cardCt - 2 );  // The previous card.
          if ( thisCard.getValue() > prevCard.getValue() ) {
             gameInProgress = false;
             message = "Izgubili ste!.";
          }
          else if ( thisCard.getValue() < prevCard.getValue() ) {
             gameInProgress = false;
             message = "Izgubili ste!";
          }
          else if ( cardCt == 4) {
             gameInProgress = false;
             message = "Pobijedili ste! Pogodili ste tri puta.";
          }
          else {
             message = "Odlièno! Pokušaj " + cardCt + ".";
          }
          repaint();
       } 
      
      /**
       * Called by the constructor, and called by actionPerformed() if
       * the use clicks the "New Game" button.  Start a new game.
       */
      void doNewGame() {
         if (gameInProgress) {
               // If the current game is not over, it is an error to try
               // to start a new game.
            message = "Još morate završiti ovu igru!";
            Toolkit.getDefaultToolkit().beep();
            repaint();
            return;
         }
         deck = new Deck();   // Create the deck and hand to use for this game.
         hand = new Hand();
         deck.shuffle();
         hand.addCard( deck.dealCard() );  // Deal the first card into the hand.
         message = "Je li sljedeæa karta veæa, manja ili ista?";
         gameInProgress = true;
         repaint();
      } // end doNewGame()
      
      
      /**
       * This method draws the message at the bottom of the
       * panel, and it draws all of the dealt cards spread out
       * across the canvas.  If the game is in progress, an extra
       * card is drawn face down representing the card to be dealt next.
       */
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (cardImages == null) {
            g.drawString("Pogreška: Slike karti se ne mogu uèitati!", 10,30);
            return;
         }
         g.setFont(bigFont);
         g.drawString(message,15,168);
         int cardCt = hand.getCardCount();
         for (int i = 0; i < cardCt; i++)
            drawCard(g, hand.getCard(i), 15 + i * (15+79), 15);
         if (gameInProgress)
            drawCard(g, null, 15 + cardCt * (15+79), 15);
      } // end paintComponent()

      
      /**
       * Draws a card in a 79x123 pixel rectangle with its
       * upper left corner at a specified point (x,y).  Drawing the card 
       * requires the image file "cards.png".
       * @param g The non-null graphics context used for drawing the card.  If g is
       * null, a NullPointerException will be thrown.
       * @param card The card that is to be drawn.  If the value is null, then a
       * face-down card is drawn.
       * @param x the x-coord of the upper left corner of the card
       * @param y the y-coord of the upper left corner of the card
       */
      public void drawCard(Graphics g, Card card, int x, int y) {
         int cx;    // x-coord of upper left corner of the card inside cardsImage
         int cy;    // y-coord of upper left corner of the card inside cardsImage
         if (card == null) {
            cy = 4*123;   // coords for a face-down card.
            cx = 2*79;
         }
         else {
            cx = (card.getValue()-1)*79;
            switch (card.getSuit()) {
            case Card.CLUBS:    
               cy = 0; 
               break;
            case Card.DIAMONDS: 
               cy = 123; 
               break;
            case Card.HEARTS:   
               cy = 2*123; 
               break;
            default:  // spades   
               cy = 3*123; 
               break;
            }
         }
         g.drawImage(cardImages,x,y,x+79,y+123,cx,cy,cx+79,cy+123,this);
      }        

   
      private void loadImage() {
         ClassLoader cl = highlow.class.getClassLoader();
         URL imageURL = cl.getResource("\\img\\cards.png");
         if (imageURL != null)
            cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
      }


   } // end nested class CardPanel
   
   
} // end class HighLowWithImages