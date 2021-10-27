package com.justdan.weblab3.result;

import com.justdan.weblab3.db.ResultDao;
import org.hibernate.HibernateException;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@ManagedBean
@SessionScoped
public class ResultBean implements Serializable {

    private static final long serialVersionUID = 6196266232630942124L;
    private ArrayList<Result> list;
    private final ResultDao resultDao = new ResultDao();

    private float x = 0.0F;
    private float y = 0.0F;
    private float r = 1.0F;

    @PostConstruct
    public void init() {
        try {
            resultDao.connect();
            list = resultDao.getAll();
        } catch (PersistenceException e) {
            resultDao.setConnected(false);
            resultDao.close();
            PrimeFaces.current().executeScript("alert('Подключение к БД не удалось')");
            list = new ArrayList<Result>();
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void onRadiusChange() {
        PrimeFaces.current().executeScript("drawArea(" + r + ")");
    }

    public void check(){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String xP = params.get("x");
        String yP = params.get("y");
        float tX;
        float tY;
        if(yP == null || xP == null) {
            tX = x;
            tY = y;
        } else {
            tX = Float.parseFloat(xP);
            tY = Float.parseFloat(yP);
            tX = tX*r;
            tY = tY*r;
            x = tX;
            y = tY;
        }
        Result result = new Result(isIn(tX, tY, r), tX, tY, r);
        try {
            if(!resultDao.isConnected()) {
                resultDao.connect();
                resultDao.setConnected(true);
                list = resultDao.getAll();
                PrimeFaces.current().executeScript("clean()");
                PrimeFaces.current().executeScript("draw(" + r + ")");
            }
            resultDao.send(result);
            list.add(result);
            PrimeFaces.current().executeScript("drawDotByClick('" + (result.isResult() ? "#37f863" : "crimson") + "'," + tX/r + ", " + tY/r + ")");
        } catch (PersistenceException e) {
            resultDao.setConnected(false);
            resultDao.close();
            PrimeFaces.current().executeScript("alert('Подключение к БД не удалось')");
        }
    }

    private boolean isIn(float x, float y, float r) {
        return Math.pow(x/r/7, 2) * Math.sqrt((Math.abs(Math.abs(x/r)-3))/(Math.abs(x/r)-3)) + Math.pow(y/r/3, 2)*Math.sqrt((Math.abs(y/r + 3* Math.sqrt(33) / 7))/(y/r + 3* Math.sqrt(33) / 7)) - 1 <= 0 |
                (Math.abs(x/r/2) - ((3*Math.sqrt(33)-7)*x/r*x/r)/112 - 3 + Math.sqrt(1 - Math.pow(Math.abs(Math.abs(x/r) - 2) - 1, 2)) - y/r <= 0 & y/r <= 0 & -3 <= y/r & -4 <= x/r & x/r <= 4) |
                (-Math.abs(x/r) / 2 - 3 * Math.sqrt(10) / 7 * Math.sqrt(4 - Math.pow(Math.abs(x/r) - 1, 2)) - y/r + 6 * Math.sqrt(10) / 7 + 1.5 >= 0 & y/r > 0)
                | (1.5 + 3*Math.abs(x/r) + 0.75 - y/r >= 0 & y/r <= 4 & y/r > 0) & (9 - 8*Math.abs(x/r) - y/r >= 0 & y/r <= 4 & y/r > 0);
    }

    public ArrayList<Result> getList() {
        return list;
    }

}
