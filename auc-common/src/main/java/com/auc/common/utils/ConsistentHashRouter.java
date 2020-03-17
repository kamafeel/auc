package com.auc.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.springframework.stereotype.Service;

/**
 * HASH一致算法,域登录地址的负载均衡
 * @param <T>
 */
@Service
public class ConsistentHashRouter<T> {

  private final ConcurrentSkipListMap<Long, T> ring = new ConcurrentSkipListMap<>();
  private final MD5Hash hashFunction = new MD5Hash();

  public void init(Collection<T> pNodes, int vNodeCount) {
    if (pNodes != null) {
      for (T pNode : pNodes) {
        addNode(pNode, vNodeCount);
      }
    }
  }

  /**
   * add physic node to the hash ring with some virtual nodes
   *
   * @param pNode physical node needs added to hash ring
   * @param vNodeCount the number of virtual node of the physical node. Value should be greater than
   * or equals to 0
   */
  public void addNode(T pNode, int vNodeCount) {
    if (vNodeCount < 0) {
      throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
    }
    int existingReplicas = getExistingReplicas(pNode);
    for (int i = 0; i < vNodeCount; i++) {
      ring.put(hashFunction.hash(pNode.toString() + (i + existingReplicas)), pNode);
    }
  }

  /**
   * remove the physical node from the hash ring
   */
  public void removeNode(T pNode) {
    Iterator<Long> it = ring.keySet().iterator();
    while (it.hasNext()) {
      Long key = it.next();
      if (ring.get(key).equals(pNode)) {
        it.remove();
      }
    }
  }

  /**
   * with a specified key, route the nearest Node instance in the current hash ring
   *
   * @param objectKey the object key to find a nearest Node
   */
  public T routeNode(String objectKey) {
    if (ring.isEmpty()) {
      return null;
    }
    Long hashVal = hashFunction.hash(objectKey);
    SortedMap<Long, T> tailMap = ring.tailMap(hashVal);
    Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
    return ring.get(nodeHashVal);
  }


  public int getExistingReplicas(T pNode) {
    int replicas = 0;
    for (T t : ring.values()) {
      if (t.equals(pNode)) {
        replicas++;
      }
    }
    return replicas;
  }


  //default hash function
  private static class MD5Hash {

    MessageDigest instance;

    public MD5Hash() {
      try {
        instance = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
      }
    }

    public long hash(String key) {
      instance.reset();
      instance.update(key.getBytes());
      byte[] digest = instance.digest();

      long h = 0;
      for (int i = 0; i < 4; i++) {
        h <<= 8;
        h |= ((int) digest[i]) & 0xFF;
      }
      return h;
    }
  }

}