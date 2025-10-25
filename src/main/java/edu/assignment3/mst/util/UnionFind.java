package edu.assignment3.mst.util;

public class UnionFind {
    private final int[] p, r;
    public long finds=0, unions=0;
    public UnionFind(int n) {
        p=new int[n];
        r=new int[n];
        for(int i=0;i<n;i++) {
            p[i]=i;
        }
    }
    public int find(int x){
        finds++;
        return (p[x]==x)? x : (p[x]=find(p[x]));
    }
    public boolean union(int a,int b){
        int ra=find(a), rb=find(b);
        if(ra==rb) return false;
        if(r[ra]<r[rb]) {
            p[ra]=rb;
        }
        else if(r[rb]<r[ra]) {
            p[rb]=ra;
        }
        else {
            p[rb]=ra;
            r[ra]++;
        }
        unions++;
        return true;
    }
}