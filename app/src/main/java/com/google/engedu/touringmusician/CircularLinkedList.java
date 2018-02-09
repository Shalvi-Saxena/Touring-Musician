/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.util.Log;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;
        Node()
        {
            this.point = null;
            this.prev = null;
            this.next = null;
        }
    }

    Node head = null;

    public void insertBeginning(Point p) {
        if(head == null)
        {
            Node n = new Node();
            n.point = p;
            n.prev = n.next = n;
            head = n;
            return;
        }
        Node last = head.prev;
        Node n = new Node();
        n.point = p;
        n.next = head;
        head.prev = n;
        n.prev = last;
        last.next = n;
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        CircularLinkedListIterator li = new CircularLinkedListIterator();
        Point from = li.next();
        while(li.hasNext()) {
            Point to = li.next();
            total += distanceBetween(from, to);
            from = to;
        }
        return total;
    }

    public void insertNearest(Point p) {
        Node curr = head;
        if(curr == null) {
            insertBeginning(p);
            return;
        }
        else if(curr.next == curr) {
            insertBeginning(p);
            return;
        }
        float min = Float.MAX_VALUE;
        Node Min_Node = head;
        float distance = distanceBetween(curr.point, p);
        while (curr.next!=head)
        {
            distance = distanceBetween(curr.point, p);
            if(min > distance)
            {
                min = distance;
                Min_Node = curr;
            }
            curr = curr.next;
        }
        Node node = new Node();
        node.point = p;
        node.next = Min_Node.next;
        node.prev = Min_Node;
        Min_Node.next = node;
    }

    public void insertSmallest(Point p) {
        if(head == null)
        {
            insertBeginning(p);
            return;
        }
        if(head.next == head)
        {
            insertBeginning(p);
            return;
        }
        float totalDistance = totalDistance();
        Log.v("message", "totalDistance");
        Node curr = head;
        float smallDistance = totalDistance + distanceBetween(head.point, p);
        boolean flag = true;
        Node Small_Node = head;
        while (curr.next != head)
        {
            float tempDistance = totalDistance + distanceBetween(curr.point, p) +
                    distanceBetween(curr.next.point, p) - distanceBetween(curr.point, curr.next.point);
            if(tempDistance < smallDistance)
            {
                smallDistance = tempDistance;
                Small_Node = curr;
                flag = false;
                Log.v("message", "smalldistance");
            }
            curr = curr.next;
        }
        float tempDistance = totalDistance + distanceBetween(head.prev.point, p);
        if(tempDistance < smallDistance)
        {
            Node n = new Node();
            n.point = p;
            n.next = head;
            n.prev = head.prev;
            head.prev.next = n;
            head.prev = n;
            return;
        }
        Node n = new Node();
        n.point = p;
        n.prev = Small_Node;
        n.next = Small_Node.next;
        Small_Node.next.prev = n;
        Small_Node.next = n;
        Log.v("message", "end");
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
