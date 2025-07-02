package com.company.was.config;

import java.util.List;
import java.util.Map;

public record Config(int port, Map<String, VirtualHost> hosts) {

    public VirtualHost getVirtualHost(String host) {
        return this.hosts().getOrDefault(host, getDefaultVirtualHost());
    }

    public List<VirtualHost> getAllVirtualHosts() {
        return List.copyOf(this.hosts().values());
    }

    private VirtualHost getDefaultVirtualHost() {
        return this.hosts().get("a.com");
    }

    public record VirtualHost(String httpRoot, Map<String, String> errorPages) {
    }
}
