//
//  MessageViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class MessageViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var chatGroups = [ChatGroupData]()

    func reload() {
        
        self.chatGroups = FetchChatGroupRequester.shared.dataList
        self.tableView.reloadData()

        if self.chatGroups.count > 0 {
            self.tableView.isHidden = false
            self.noDataLabel.isHidden = true
        } else {
            self.tableView.isHidden = true
            self.noDataLabel.isHidden = false
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.reload()

    }


}

extension MessageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
}
