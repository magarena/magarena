# reads lines from stdin representing requests
# outputs percentage of cache misses for various cache sizes

import collections

class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = collections.OrderedDict()
        self.misses = 0

    def get(self, key):
        try:
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        except KeyError:
            self.misses += 1
            return -1

    def set(self, key, value):
        try:
            self.cache.pop(key)
        except KeyError:
            if len(self.cache) >= self.capacity:
                self.cache.popitem(last=False)
        self.cache[key] = value

caches = [LRUCache(i*10) for i in range(1, 11)]

import sys

seen = set()

cnt = 0
for line in sys.stdin:
    cnt += 1
    for c in caches:
        if line in seen:
            c.get(line)
        c.set(line, 0)
    seen.add(line)

for c in caches:
    print c.capacity, c.misses * 100.0 / cnt
