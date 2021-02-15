#import
import ufw_gen as ufw
import win_gen as win
import iptb_gen as iptb

#rule entity
class entity:
    def __init__(self, win_ip, lin_ip, src_port, dst_port):
        self.win_ip = win_ip
        self.lin_ip = lin_ip
        self.src_port = src_port
        self.dst_port = dst_port

#DMZ
#src_port - the port in which the message should be received
#dst port - the reply
web = entity("125.168.0.0-126.168.3.255", "125.158.0.0/22", ["80", "8080", "403"], None)
email = entity("125.168.4.0-125.168.7.255", "125.168.4.0/22", ["25", "110", "995"], None)
ftp = entity("125.168.7.0-125.168.7.255", "125.168.7.0/24", ["23"], None)
dns = entity("125.168.8.0-125.168.9.255", "125.168.8.0/23", ["53"], None)
proxy = entity("125.168.10.0-125.168.10.255", "125.168.10.0/24", ["53"], None)

#Internal
#no ports
sales = entity("125.168.11.0-125.168.11.255", "125.168.11.0/24", None, None)
hr = entity("125.168.12.0-125.168.12.255", "125.168.12.0/24", None, None)
accounting = entity("125.168.13.0-125.168.13.255", "125.168.13.0/24", None, None)
devs = entity("125.168.14.0-125.168.15.255", "125.168.14.0/23", None, None)
prod = entity("125.168.16.0-125.168.17.255", "125.168.16.0/23", None, None)
rd = entity("125.168.18.0-125.168.19.255", "125.168.18.0/23", None, None)
eng = entity("125.168.20.0-125.168.21.255", "125.168.20.0/23", None, None)
internal = entity("125.168.22.0-125.168.23.255", "125.168.22.0/23", None, None)
quality = entity("125.168.24.0-125.168.24.255", "125.168.24.0/24", None, None)
it = entity("125.168.25.0-125.168.25.255", "125.168.25.0/24", None, None)
test_net = entity("125.168.26.0-125.168.26.255", "125.168.26.0/24", None, None)

#External service
azure = entity("13.64.0.0-13.127.255.255", "13.64.0.0/10", ["80", "8080", "403"], ["2500"])
google = entity("172.217.0.0-172.217.255.255", "172.217.0.0/16", ["80", "8080", "403"], ["2500"])
stackoverflow = entity("151.101.0.0-151.101.255.255", "151.101.0.0/16", ["80", "8080", "403"], ["2500"])
ms = entity("40.64.0.0-40.127.255.255", "40.74.0.0/10", ["80", "8080", "403"], ["2500"])
wiki = entity("208.80.152.0-208.80.155.255", "208.80.152.0/22", ["80", "8080", "403"], ["2500"])
external = [azure, google, stackoverflow, ms, wiki]

#vpn clients
vpn1 = entity("172.2.1.3", "172.2.1.3", None, ["53", "443", "110", "80", "502", "501", "80"])
vpn2 = entity("172.2.10.0-172.2.11.255", "172.2.10.0/23", None, ["53", "443", "110", "80", "502", "501", "80"])
vpn3 = entity("192.0.3.1", "192.0.3.1", None, ["53", "443", "110", "80", "502", "501", "80"])
vpn4 = entity("198.10.55.0-198.10.55.255", "198.10.55.0/23", None, ["53", "443", "110", "80", "502", "501", "80"])
vpn = [vpn1, vpn2, vpn3, vpn4]

#Internal
# sales, hr, accounting
#   web, email, dns, external services
#
# dev, prod, rd, eng, internal, it
#   web, email, ftp, dns, proxy, external services
#
# quality, test
#   web, email, ftp, dns
#


#External
# + internal
# 
# + anomalies
#
# VPN (external only)
#   DNS


#Internal service interaction
def win_internal_reply_response(client_name, client, services):
    x = 0
    for service in services:
        y = 0
        for src_port in service.src_port:
            win.add_rule(client_name + "_service" + str(x) + "." + str(y), "out", "allow", client.win_ip, service.win_ip, None, src_port)
            y += 1
        win.add_rule("service_" + client_name + str(x), "in", "allow", service.win_ip, client.win_ip, None, None)
        x += 1

#external service interaction
def win_external_reply_response(client_name, client, decision):
    x = 0
    for service in external:
        y = 0
        for src_port in service.src_port:
            win.add_rule(client_name + "_external_service" + str(x) + "." + str(y), "out", decision, client.win_ip, service.win_ip, None, src_port)
            y += 1

        y = 0
        for dst_port in service.dst_port:
            win.add_rule("external_service_" + client_name + str(x) + "." + str(y), "in", decision, service.win_ip, client.win_ip, None, dst_port)
            y += 1
            
        x += 1

#Create firewall rules for internal firewall
def internal_fire_win():
    # sales, hr, accounting
    win_internal_reply_response("sales", sales, [web, email, dns]) #sales
    win_external_reply_response("sales", sales, "allow")
    win_internal_reply_response("hr", hr, [web, email, dns]) #hr
    win_external_reply_response("hr", hr, "allow")
    win_internal_reply_response("accounting", accounting, [web, email, dns]) #accounting
    win_external_reply_response("accounting", accounting, "allow")

    # dev, prod, rd, eng, internal, it
    win_internal_reply_response("dev", devs, [web, email, ftp, dns, proxy]) #dev
    win_external_reply_response("dev", devs, "allow")
    win_internal_reply_response("prod", prod, [web, email, ftp, dns, proxy]) #prod
    win_external_reply_response("prod", prod, "allow")
    win_internal_reply_response("rd", rd, [web, email, ftp, dns, proxy]) #rd
    win_external_reply_response("rd", rd, "allow")
    win_internal_reply_response("eng", eng, [web, email, ftp, dns, proxy]) #eng
    win_external_reply_response("eng", eng, "allow")
    win_internal_reply_response("internal", internal, [web, email, ftp, dns, proxy]) #internal
    win_external_reply_response("internal", internal, "allow")
    win_internal_reply_response("it", it, [web, email, ftp, dns, proxy]) #it
    win_external_reply_response("it", it, "allow")

    #quality, testing
    win_internal_reply_response("test", test_net, [web, email, ftp, dns]) #test
    win_internal_reply_response("quality", quality, [web, email, ftp, dns]) #quality

def external_fire_win():
    #VPN clients
    x = 0
    for v in vpn:
        y = 0
        for dst_port in v.dst_port:
            win.add_rule("vpn" + str(x) + "." + str(y), "in", "allow", v.win_ip, dns.win_ip, None, dst_port)
            y += 1
        x += 1

    #Redundant
    #same rules
    win_external_reply_response("redundant_accounting", accounting, "allow")

    #inconsistent
    #rules where they have the same packet but different action
    win_external_reply_response("inconsistent_prod", prod, "block")
    
    #correlation
    #rules that intersect with another but defines a different action
    devs_prod_corr = entity("125.168.15.0-125.168.16.255", None, None, None)
    win_external_reply_response("devs_prod_corr", devs_prod_corr, "block")

    #partial_redundancy
    #rules that intersect with another but defines the same action
    rd_eng_partial_redundancy = entity("125.168.19.0-125.168.20.255", None, None, None)
    win_external_reply_response("rd_eng_partial_redundancy", rd_eng_partial_redundancy, "allow")

    #generalization
    #rules where one is a subset of another but they have different actions and the superset rule has a higher priority
    google2 = entity("172.217.0.0-172.217.127.255", "172.217.0.0/17", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in google2.src_port:
        win.add_rule("generalization_prod_google" + str(x), "out", "block", prod.win_ip, google2.win_ip, None, src_port)
        x += 1
    win.add_rule("generalization_google_prod", "in", "block", google2.win_ip, prod.win_ip, None, google.dst_port)

    #up_redundancy
    #rules where one is a subset of another but they have the same actions and the superset rule has a higher priority
    stackoverflow2 = entity("151.101.0.0-151.101.127.255", "151.101.0.0/17", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in stackoverflow2.src_port:
        win.add_rule("up_redundancy_it_stackoverflow" + str(x), "out", "allow", it.win_ip, stackoverflow2.win_ip, None, src_port)
        x += 1
    win.add_rule("up_redundancy_stackoverflow_it", "in", "allow", stackoverflow2.win_ip, it.win_ip, None, stackoverflow2.dst_port)
    
    #shadowing
    #rules where one is a subset of another but they have different actions and the subset rule has a higher priority
    wiki2 = entity("208.80.144.0-208.80.159.255", "208.80.152.0/20", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in stackoverflow2.src_port:
        win.add_rule("shadowing_it_wiki" + str(x), "out", "block", it.win_ip, wiki2.win_ip, None, src_port)
        x += 1
    win.add_rule("shadowing_wiki_it", "in", "block", wiki2.win_ip, it.win_ip, None, wiki2.dst_port)

    #down_redundancy
    #rules where one is a subset of another but they have the same actions and the subset rule has a higher priority
    proxy2 = entity("125.168.10.0-125.168.11.255", "125.168.10.0/23", ["53"], None)
    win_external_reply_response("down_redundancy", proxy2, "allow")

def main():
    firewall = input("win = 0, ufw = 1, iptable = 2\n")

    if firewall == "0":
        internal_fire_win()
        win.export()
        external_fire_win()
        win.export2()
    elif firewall == "1":
        x = 1
        # ufw.export()
    else:
        x = 2
        #iptb.export()

if __name__ == "__main__":
    main()