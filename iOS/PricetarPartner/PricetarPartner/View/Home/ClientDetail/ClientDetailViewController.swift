//
//  ClientDetailViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class ClientDetailViewController: UIViewController {
    
    @IBOutlet private weak var headerNameLabel: UILabel!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var statusLabel: UILabel!
    @IBOutlet private weak var lastLoginDatetimeLabel: UILabel!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var profileTypeLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var useFrequencyLabel: UILabel!
    @IBOutlet private weak var newConditionLabel: UILabel!
    @IBOutlet private weak var oldConditionLabel: UILabel!
    @IBOutlet private weak var genreLabel: UILabel!
    @IBOutlet private weak var optionLabel: UILabel!
    
    private var user: UserData!
    
    func set(user: UserData) {
        self.user = user
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.headerNameLabel.text = self.user.nickname
        
        ImageStorage.shared.fetch(url: UserData.imageUrl(userId: self.user.id), imageView: self.userImageView)
        self.userImageView.layer.cornerRadius = (UIScreen.main.bounds.size.width - 120) / 2
        
        self.nameLabel.text = self.user.nickname
        self.lastLoginDatetimeLabel.text = self.user.lastLoginString()
        self.areaLabel.text = self.user.area
        self.messageLabel.text = self.user.clientMessage
        self.useFrequencyLabel.text = self.user.clientUseFrequency
        self.newConditionLabel.text = self.user.clientNewCondition
        self.oldConditionLabel.text = self.user.clientOldCondition
        self.genreLabel.text = self.user.clientGenres.joined(separator: "・")
        self.optionLabel.text = self.user.clientOptions.joined(separator: "\n")
    }
    

    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapMessage(_ sender: Any) {
        
    }
}

