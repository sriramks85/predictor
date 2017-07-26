package com.sriramks
/**
 * Created by a1257317 on 7/24/17.
 */
class PredictNext {


    public static void  main(String [] a) {
        PredictTree tree = new PredictTree()
        tree.addValues("12345","1")
        tree.addValues("12345","2")
        tree.addValues("11111","1")
        tree.addValues("22222","2")
        tree.addValues("33333","2")
        tree.addValues("44444","2")
        tree.addValues("12345","3")
        tree.addValues("22222","3")


        System.out.println("Next sku user:3 will visit is "+tree.findNextBestMatch("3").skuId)
    }

}
