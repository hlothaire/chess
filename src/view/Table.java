package view;

import com.google.common.collect.Lists;
import modele.board.Board;
import modele.board.BoardUtils;
import modele.board.Move;
import modele.board.Tile;
import modele.pieces.Piece;
import modele.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final static String defaultPieceImagesPath = "ressources/";
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    public Table(){
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("open up pgn file ");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves",false);
        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                highlightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckBox);
        return preferencesMenu;
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        public BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_TILES;i++){
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;


        public TilePanel(final BoardPanel boardPanel, final int tileId){
           super(new GridBagLayout());
           this.tileId = tileId;
           setPreferredSize(TILE_PANEL_DIMENSION);
           assignTileColor();
           assignTilePieceIcon(chessBoard);
           addMouseListener(new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent mouseEvent) {
                   if(isRightMouseButton(mouseEvent)) {
                       sourceTile = null;
                       destinationTile = null;
                       humanMovedPiece = null;
                   }else if (isLeftMouseButton(mouseEvent)){
                       if(sourceTile == null){
                           sourceTile = chessBoard.getTile(tileId);
                           humanMovedPiece = sourceTile.getPiece();
                           if(humanMovedPiece == null){
                               sourceTile = null;
                           }
                       } else {
                           destinationTile = chessBoard.getTile(tileId);
                           final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),
                                   destinationTile.getTileCoordinate());
                           final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                           if(transition.getMoveStatus().isDone()){
                               chessBoard = transition.getTransitionBoard();
                           }
                           sourceTile = null;
                           destinationTile = null;
                           humanMovedPiece = null;
                       }
                       SwingUtilities.invokeLater(new Runnable() {
                           @Override
                           public void run() {
                               boardPanel.drawBoard(chessBoard);
                           }
                       });
                   }
               }


               @Override
               public void mousePressed(MouseEvent mouseEvent) {

               }

               @Override
               public void mouseReleased(MouseEvent mouseEvent) {

               }

               @Override
               public void mouseEntered(MouseEvent mouseEvent) {

               }

               @Override
               public void mouseExited(MouseEvent mouseEvent) {

               }
           });
           validate();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try{
                final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).
                        getPiece().getPieceAlliance().toString().substring(0,1) + board.getTile(this.tileId).getPiece().toString() + ".gif"));
                add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }

        private void assignTileColor(){
            if(BoardUtils.EIGHTH_RANK[this.tileId] ||
              BoardUtils.SIXTH_RANK[this.tileId] ||
              BoardUtils.FOURTH_RANK[this.tileId] ||
              BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.SEVEN_RANK[this.tileId] ||
                      BoardUtils.FIFTH_RANK[this.tileId] ||
                      BoardUtils.THIRD_RANK[this.tileId] ||
                      BoardUtils.FIRST_RANK[this.tileId]){
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }

        private void highlightLegal(final Board board){
            if(highlightLegalMoves){
                for(final Move move : pieceLegalMove(board)){
                    if(move.getDestinationCoordinate() == this.tileId){
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("ressources/green_dot.png")))));
                        } catch(Exception e){
                            System.out.println(e.toString());
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMove(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegal(board);
            validate();
        }
    }
}
