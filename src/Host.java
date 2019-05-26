class Host implements Comparable<Host> {
    private String ip;
    long wvalue = 0;

    public Host(String ip) {
        this.ip = ip;
           String[] thisoctetlist = ip.split("\\.");
           
    for (int i = 0; i < 4; i++) {
                  // System.out.println(thisoctetlist[i]);

            int power = 3 - i;
                   //    System.out.println(power);

            wvalue += Integer.parseInt(thisoctetlist[i]) * Math.pow(256, power); 
//System.out.println(wvalue);

        
    }
    
    }
    
    public String toString() {
        return ip;
    }
    
    public int compareTo(Host other) {
        
        return (int) (wvalue - other.wvalue);
     
    }
}