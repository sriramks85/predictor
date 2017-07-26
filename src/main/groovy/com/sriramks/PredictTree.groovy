package com.sriramks
/**
 * Created by a1257317 on 7/24/17.
 */
class PredictTree {

    List<PredictNode> skus
    Map<String, PredictNode> lastAccessedSku = [:]

    PredictNode rootNode = new PredictNode(skuId: null, nodeVisitors: 0, nodeName:'rootNode', probability: 1)

    //Probability calculation - no of users came to parent node to no of users came to currentNode
    void computeProbability(int parentNodeVisitors, PredictNode predictNode) {
        predictNode.probability = predictNode.nodeVisitors/parentNodeVisitors
    }

    void addValues(String skuId, String userId) {
        rootNode.nodeVisitors++
        //  Find the lastAccessedSku by User
        //  If the lastAccessedSku does not exist then fetch the root PredictNode from the rootNode and add the User to it and recompute probability, also update the lastAccessedSku
        //  If lastAccessedSku exist then try finding the sku in immediate child nodes
        // If sku exists then add the userId to it and recompute the nodes probability, also update lastAccessedSku for the User
        // If sku does not exist then add a new child node to the last accessed node with skuId and userId and probability
        PredictNode predictNode = lastAccessedSku.get(userId)
        if(predictNode) {
            computePredictNode(predictNode, skuId, userId)
        }else {
            computePredictNode(rootNode, skuId, userId)
        }

    }

    private void computePredictNode(PredictNode predictNode, String skuId, String userId) {
        predictNode.nodeVisitors++
        PredictNode exactNode = predictNode?.childNodes?.find { PredictNode node ->
            node.skuId == skuId
        }
        if (exactNode) {
            exactNode.userIds.add(userId)
            exactNode.nodeVisitors++
            computeProbability(predictNode.nodeVisitors, exactNode)
            lastAccessedSku.put(userId, exactNode)
        } else {
            exactNode = new PredictNode(skuId: skuId, nodeVisitors: 1, userIds: [userId])
            computeProbability(predictNode.nodeVisitors, exactNode)
            predictNode.childNodes.add(exactNode)
            lastAccessedSku.put(userId, exactNode)
        }
    }

    PredictNode findNextBestMatch(String userId) {
        PredictNode result = null
        PredictNode lastAccessedNode = lastAccessedSku.get(userId)
            int maxProbability = 0
            lastAccessedNode?.childNodes?.each { PredictNode node ->
                if(node.probability > maxProbability) {
                    maxProbability = node.probability
                    result = node
                }
            }
        result
    }

}

class PredictNode {
    String skuId
    int nodeVisitors
    String nodeName
    List<String> userIds = []
    List<PredictNode> childNodes = []
    float probability
}




