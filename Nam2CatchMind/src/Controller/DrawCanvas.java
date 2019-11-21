package Controller;

import java.util.ArrayList;

import Model.DrowInfoVO;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/*
 * 그림그리기에 사용되는 클래스들
 * 
 * DrawCanvas (실제캔버스에 그림을 그리기 위한 라인이 만들어지는 곳) arPt : 마우스커서의 좌표 리스트 paint : 내장되어
 * 있는 함수들을 모아서 함수화 한 것 for문에서 true값인 좌표들을 선별해서 그려지도록 함
 * 
 * DrowInfoVO : 그리는 정보에 대한 값에 대한 클래스 x, y, 그린행위에 대해서 boolean 값, color : int 값으로
 * 저장됨 getDBColor() : case문, 색깔을 리턴해줌
 * 
 */

public class DrawCanvas extends Canvas {
	ArrayList<DrowInfoVO> arPt;

	DrawCanvas(ArrayList<DrowInfoVO> arPt) {
		this.arPt = arPt;
	}

	public void paint(GraphicsContext g) {
		g.setLineWidth(2.0);

		for (int i = 0; i < arPt.size() - 1; i++) {
			if (arPt.get(i).isDraw()) {
				int color = arPt.get(i).getDBColor();
				Color penColor = arPt.get(i).getColor(color);
				g.setStroke(penColor);
				g.strokeLine(arPt.get(i).getX(), arPt.get(i).getY(), arPt.get(i + 1).getX(), arPt.get(i + 1).getY());
			}
		}
	}
}