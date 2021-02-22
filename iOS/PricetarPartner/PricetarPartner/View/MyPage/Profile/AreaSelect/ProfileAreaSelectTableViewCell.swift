//
//  ProfileAreaSelectTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class ProfileAreaSelectTableViewCell: UITableViewCell {

    @IBOutlet private weak var selectedView: UIView!
    @IBOutlet private weak var areaLabel: UILabel!

    func configure(isSelected: Bool, area: String) {
        
        self.selectedView.isHidden = !isSelected
        self.areaLabel.text = area
    }
}
