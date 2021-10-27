package com.justdan.weblab3.result;

import javax.persistence.*;

@Entity
@Table(name = "POINTS")
public class Result {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RES")
    private boolean result;

    @Column(name = "X")
    private float x;

    @Column(name = "Y")
    private float y;

    @Column(name = "R")
    private float r;

    public Result() {
    }

    public Result(boolean result, float x, float y, float r) {
        this.result = result;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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
}
