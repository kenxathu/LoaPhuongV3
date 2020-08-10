PrimeFaces.widget.Dialog = PrimeFaces.widget.BaseWidget.extend({
    init: function(a) {
        this._super(a);
        this.content = this.jq.children(".ui-dialog-content");
        this.titlebar = this.jq.children(".ui-dialog-titlebar");
        this.footer = this.jq.find(".ui-dialog-footer");
        this.icons = this.titlebar.children(".ui-dialog-titlebar-icon");
        this.closeIcon = this.titlebar.children(".ui-dialog-titlebar-close");
        this.minimizeIcon = this.titlebar.children(".ui-dialog-titlebar-minimize");
        this.maximizeIcon = this.titlebar.children(".ui-dialog-titlebar-maximize");
        this.blockEvents = "focus." + this.id + " mousedown." + this.id + " mouseup." + this.id;
        this.resizeNS = "resize." + this.id;
        this.cfg.absolutePositioned = this.jq.hasClass("ui-dialog-absolute");
        this.cfg.width = this.cfg.width || "auto";
        this.cfg.height = this.cfg.height || "auto";
        this.cfg.draggable = this.cfg.draggable === false ? false : true;
        this.cfg.resizable = this.cfg.resizable === false ? false : true;
        this.cfg.minWidth = this.cfg.minWidth || 150;
        this.cfg.minHeight = this.cfg.minHeight || this.titlebar.outerHeight();
        this.cfg.position = this.cfg.position || "center";
        this.parent = this.jq.parent();
        this.initSize();
        this.bindEvents();
        if (this.cfg.draggable) {
            this.setupDraggable()
        }
        if (this.cfg.resizable) {
            this.setupResizable()
        }
        if (this.cfg.modal) {
            this.syncWindowResize()
        }
        if (this.cfg.appendTo) {
            this.jq.appendTo(PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.appendTo))
        }
        if ($(document.body).children(".ui-dialog-docking-zone").length === 0) {
            $(document.body).append('<div class="ui-dialog-docking-zone"></div>')
        }
        var b = $(this.jqId + "_modal");
        if (b.length > 0) {
            b.remove()
        }
        this.applyARIA();
        if (this.cfg.visible) {
            this.show()
        }
    },
    refresh: function(a) {
        this.positionInitialized = false;
        this.loaded = false;
        $(document).off("keydown.dialog_" + a.id);
        if (a.appendTo) {
            var b = $("[id=" + a.id.replace(/:/g, "\\:") + "]");
            if (b.length > 1) {
                PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(a.appendTo).children(this.jqId).remove()
            }
        }
        this.init(a)
    },
    initSize: function() {
        this.jq.css({
            width: this.cfg.width,
            height: "auto"
        });
        this.content.height(this.cfg.height);
        if (this.cfg.fitViewport) {
            this.fitViewport()
        }
        if (this.cfg.width === "auto" && PrimeFaces.isIE(7)) {
            this.jq.width(this.content.outerWidth())
        }
    },
    fitViewport: function() {
        var b = $(window).height(),
                a = this.content.innerHeight() - this.content.height();
        if (this.jq.innerHeight() > b) {
            this.content.height(b - this.titlebar.innerHeight() - a)
        }
    },
    enableModality: function() {
        var b = this,
                a = $(document);
        $(document.body).append('<div id="' + this.id + '_modal" class="ui-widget-overlay"></div>').children(this.jqId + "_modal").css({
            width: a.width(),
            height: a.height(),
            "z-index": this.jq.css("z-index") - 1
        });
        a.on("keydown." + this.id, function(g) {
            var h = $(g.target);
            if (g.keyCode === $.ui.keyCode.TAB) {
                var f = b.jq.find(":tabbable").add(b.footer.find(":tabbable"));
                if (f.length) {
                    var c = f.filter(":first"),
                            e = f.filter(":last"),
                            d = null;
                    if (c.is(":radio")) {
                        d = f.filter('[name="' + c.attr("name") + '"]').filter(":checked");
                        if (d.length > 0) {
                            c = d
                        }
                    }
                    if (e.is(":radio")) {
                        d = f.filter('[name="' + e.attr("name") + '"]').filter(":checked");
                        if (d.length > 0) {
                            e = d
                        }
                    }
                    if (h.is(document.body)) {
                        c.focus(1);
                        g.preventDefault()
                    } else {
                        if (g.target === e[0] && !g.shiftKey) {
                            c.focus(1);
                            g.preventDefault()
                        } else {
                            if (g.target === c[0] && g.shiftKey) {
                                e.focus(1);
                                g.preventDefault()
                            }
                        }
                    }
                }
            } else {
                if (!h.is(document.body) && (h.zIndex() < b.jq.zIndex())) {
                    g.preventDefault()
                }
            }
        }).on(this.blockEvents, function(c) {
            if ($(c.target).zIndex() < b.jq.zIndex()) {
                c.preventDefault()
            }
        })
    },
    disableModality: function() {
        $(document.body).children(this.jqId + "_modal").remove();
        $(document).off(this.blockEvents).off("keydown." + this.id)
    },
    syncWindowResize: function() {
        $(window).resize(function() {
            $(document.body).children(".ui-widget-overlay").css({
                width: $(document).width(),
                height: $(document).height()
            })
        })
    },
    show: function() {
        if (this.isVisible()) {
            return
        }
        if (!this.loaded && this.cfg.dynamic) {
            this.loadContents()
        } else {
            if (!this.positionInitialized) {
                this.initPosition()
            }
            this._show()
        }
    },
    _show: function() {
        this.moveToTop();
        if (this.cfg.absolutePositioned) {
            var a = $(window).scrollTop();
            this.jq.css("top", parseFloat(this.jq.css("top")) + (a - this.lastScrollTop) + "px");
            this.lastScrollTop = a
        }
        if (this.cfg.showEffect) {
            var b = this;
            this.jq.show(this.cfg.showEffect, null, "normal", function() {
                b.postShow()
            })
        } else {
            this.jq.show();
            this.postShow()
        }
        if (this.cfg.modal) {
            this.enableModality()
        }
    },
    postShow: function() {
        PrimeFaces.invokeDeferredRenders(this.id);
        if (this.cfg.onShow) {
            this.cfg.onShow.call(this)
        }
        this.jq.attr({
            "aria-hidden": false,
            "aria-live": "polite"
        });
        this.applyFocus();
        if (1 == 1) {
            this.bindResizeListener()
        }
    },
    hide: function() {
        if (!this.isVisible()) {
            return
        }
        if (this.cfg.hideEffect) {
            var a = this;
            this.jq.hide(this.cfg.hideEffect, null, "normal", function() {
                if (a.cfg.modal) {
                    a.disableModality()
                }
                a.onHide()
            })
        } else {
            this.jq.hide();
            if (this.cfg.modal) {
                this.disableModality()
            }
            this.onHide()
        }
    },
    applyFocus: function() {
        if (this.cfg.focus) {
            PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.focus).focus()
        } else {
            this.jq.find(":not(:submit):not(:button):not(:radio):not(:checkbox):input:visible:enabled:first").focus()
        }
    },
    bindEvents: function() {
        var a = this;
        this.jq.mousedown(function(b) {
            if (!$(b.target).data("primefaces-overlay-target")) {
                a.moveToTop()
            }
        });
        this.icons.on("mouseover", function() {
            $(this).addClass("ui-state-hover")
        }).on("mouseout", function() {
            $(this).removeClass("ui-state-hover")
        }).on("focus", function() {
            $(this).addClass("ui-state-focus")
        }).on("blur", function() {
            $(this).removeClass("ui-state-focus")
        });
        this.closeIcon.on("click", function(b) {
            a.hide();
            b.preventDefault()
        });
        this.maximizeIcon.click(function(b) {
            a.toggleMaximize();
            b.preventDefault()
        });
        this.minimizeIcon.click(function(b) {
            a.toggleMinimize();
            b.preventDefault()
        });
        if (this.cfg.closeOnEscape) {
            $(document).on("keydown.dialog_" + this.id, function(d) {
                var c = $.ui.keyCode,
                        b = parseInt(a.jq.css("z-index")) === PrimeFaces.zindex;
                if (d.which === c.ESCAPE && a.isVisible() && b) {
                    a.hide()
                }
            })
        }
    },
    setupDraggable: function() {
        var a = this;
        this.jq.draggable({
            cancel: ".ui-dialog-content, .ui-dialog-titlebar-close",
            handle: ".ui-dialog-titlebar",
            containment: "document",
            stop: function(d, e) {
                if (a.hasBehavior("move")) {
                    var b = a.cfg.behaviors.move;
                    var c = {
                        params: [{
                                name: a.id + "_top",
                                value: e.offset.top
                            }, {
                                name: a.id + "_left",
                                value: e.offset.left
                            }]
                    };
                    b.call(a, c)
                }
            }
        })
    },
    setupResizable: function() {
        var a = this;
        this.jq.resizable({
            handles: "n,s,e,w,ne,nw,se,sw",
            minWidth: this.cfg.minWidth,
            minHeight: this.cfg.minHeight,
            alsoResize: this.content,
            containment: "document",
            start: function(b, c) {
                a.jq.data("offset", a.jq.offset())
            },
            stop: function(b, c) {
                var d = a.jq.data("offset");
                a.jq.css("position", "fixed");
                a.jq.offset(d)
            }
        });
        this.resizers = this.jq.children(".ui-resizable-handle")
    },
    initPosition: function() {
        var a = this;
        this.jq.css({
            left: 0,
            top: 0
        });
        if (/(center|left|top|right|bottom)/.test(this.cfg.position)) {
            this.cfg.position = this.cfg.position.replace(",", " ");
            this.jq.position({
                my: "center",
                at: this.cfg.position,
                collision: "fit",
                of: window,
                using: function(f) {
                    var g = f.left < 0 ? 0 : f.left,
                            h = f.top < 0 ? 0 : f.top,
                            e = $(window).scrollTop();
                    if (a.cfg.absolutePositioned) {
                        h += e;
                        a.lastScrollTop = e
                    }
                    $(this).css({
                        left: g,
                        top: h
                    })
                }
            })
        } else {
            var d = this.cfg.position.split(","),
                    c = $.trim(d[0]),
                    b = $.trim(d[1]);
            this.jq.offset({
                left: c,
                top: b
            })
        }
        this.positionInitialized = true
    },
    onHide: function(c, a) {
        if (this.cfg.behaviors) {
            var b = this.cfg.behaviors.close;
            if (b) {
                b.call(this)
            }
        }
        this.jq.attr({
            "aria-hidden": true,
            "aria-live": "off"
        });
        if (this.cfg.onHide) {
            this.cfg.onHide.call(this, c, a)
        }
        if (1 == 1) {
            this.unbindResizeListener()
        }
    },
    moveToTop: function() {
        this.jq.css("z-index", ++PrimeFaces.zindex)
    },
    toggleMaximize: function() {
        if (this.minimized) {
            this.toggleMinimize()
        }
        if (this.maximized) {
            this.jq.removeClass("ui-dialog-maximized");
            this.restoreState();
            this.maximizeIcon.children(".ui-icon").removeClass("ui-icon-newwin").addClass("ui-icon-extlink");
            this.maximized = false
        } else {
            this.saveState();
            var b = $(window);
            this.jq.addClass("ui-dialog-maximized").css({
                width: b.width() - 6,
                height: b.height()
            }).offset({
                top: b.scrollTop(),
                left: b.scrollLeft()
            });
            this.content.css({
                width: "auto",
                height: "auto"
            });
            this.maximizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-extlink").addClass("ui-icon-newwin");
            this.maximized = true;
            if (this.cfg.behaviors) {
                var a = this.cfg.behaviors.maximize;
                if (a) {
                    a.call(this)
                }
            }
        }
    },
    toggleMinimize: function() {
        var b = true,
                a = $(document.body).children(".ui-dialog-docking-zone");
        if (this.maximized) {
            this.toggleMaximize();
            b = false
        }
        var c = this;
        if (this.minimized) {
            this.jq.appendTo(this.parent).removeClass("ui-dialog-minimized").css({
                position: "fixed",
                "float": "none"
            });
            this.restoreState();
            this.content.show();
            this.minimizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-plus").addClass("ui-icon-minus");
            this.minimized = false;
            if (this.cfg.resizable) {
                this.resizers.show()
            }
        } else {
            this.saveState();
            if (b) {
                this.jq.effect("transfer", {
                    to: a,
                    className: "ui-dialog-minimizing"
                }, 500, function() {
                    c.dock(a);
                    c.jq.addClass("ui-dialog-minimized")
                })
            } else {
                this.dock(a)
            }
        }
    },
    dock: function(a) {
        a.css("z-index", this.jq.css("z-index"));
        this.jq.appendTo(a).css("position", "static");
        this.jq.css({
            height: "auto",
            width: "auto",
            "float": "left"
        });
        this.content.hide();
        this.minimizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-minus").addClass("ui-icon-plus");
        this.minimized = true;
        if (this.cfg.resizable) {
            this.resizers.hide()
        }
        if (this.cfg.behaviors) {
            var b = this.cfg.behaviors.minimize;
            if (b) {
                b.call(this)
            }
        }
    },
    saveState: function() {
        this.state = {
            width: this.jq.width(),
            height: this.jq.height(),
            contentWidth: this.content.width(),
            contentHeight: this.content.height()
        };
        var a = $(window);
        this.state.offset = this.jq.offset();
        this.state.windowScrollLeft = a.scrollLeft();
        this.state.windowScrollTop = a.scrollTop()
    },
    restoreState: function() {
        this.jq.width(this.state.width).height(this.state.height);
        this.content.width(this.state.contentWidth).height(this.state.contentHeight);
        var a = $(window);
        this.jq.offset({
            top: this.state.offset.top + (a.scrollTop() - this.state.windowScrollTop),
            left: this.state.offset.left + (a.scrollLeft() - this.state.windowScrollLeft)
        })
    },
    loadContents: function() {
        var b = this,
                a = {
            source: this.id,
            process: this.id,
            update: this.id,
            params: [{
                    name: this.id + "_contentLoad",
                    value: true
                }],
            onsuccess: function(d, e, c) {
                PrimeFaces.ajax.Response.handle(d, e, c, {
                    widget: b,
                    handle: function(f) {
                        this.content.html(f)
                    }
                });
                return true
            },
            oncomplete: function() {
                b.loaded = true;
                b.show()
            }
        };
        PrimeFaces.ajax.Request.handle(a)
    },
    applyARIA: function() {
        this.jq.attr({
            role: "dialog",
            "aria-labelledby": this.id + "_title",
            "aria-hidden": !this.cfg.visible
        });
        this.titlebar.children("a.ui-dialog-titlebar-icon").attr("role", "button")
    },
    hasBehavior: function(a) {
        if (this.cfg.behaviors) {
            return this.cfg.behaviors[a] != undefined
        }
        return false
    },
    isVisible: function() {
        return this.jq.is(":visible")
    },
    bindResizeListener: function() {
        var a = this;
        $(window).on(this.resizeNS, function() {
            a.initPosition()
        })
    },
    unbindResizeListener: function() {
        $(window).off(this.resizeNS)
    }
});
PrimeFaces.widget.SelectOneMenu = PrimeFaces.widget.BaseWidget.extend({
    init: function(e) {
        this._super(e);
        this.panelId = this.jqId + "_panel";
        this.input = $(this.jqId + "_input");
        this.focusInput = $(this.jqId + "_focus");
        this.label = this.jq.find(".ui-selectonemenu-label");
        this.menuIcon = this.jq.children(".ui-selectonemenu-trigger");
        this.panel = this.jq.children(this.panelId);
        this.disabled = this.jq.hasClass("ui-state-disabled");
        this.itemsWrapper = this.panel.children(".ui-selectonemenu-items-wrapper");
        this.itemsContainer = this.itemsWrapper.children(".ui-selectonemenu-items");
        this.items = this.itemsContainer.find(".ui-selectonemenu-item");
        this.options = this.input.children("option");
        this.cfg.effect = this.cfg.effect || "fade";
        this.cfg.effectSpeed = this.cfg.effectSpeed || "normal";
        this.optGroupsSize = this.itemsContainer.children("li.ui-selectonemenu-item-group").length;
        var d = this,
                b = this.options.filter(":selected"),
                c = this.items.eq(b.index());
        this.options.filter(":disabled").each(function() {
            d.items.eq($(this).index()).addClass("ui-state-disabled")
        });
        this.triggers = this.cfg.editable ? this.jq.find(".ui-selectonemenu-trigger") : this.jq.find(".ui-selectonemenu-trigger, .ui-selectonemenu-label");
        if (this.cfg.editable) {
            var g = this.label.val();
            if (g === b.text()) {
                this.highlightItem(c)
            } else {
                this.items.eq(0).addClass("ui-state-highlight");
                this.customInput = true;
                this.customInputVal = g
            }
        } else {
            this.highlightItem(c)
        }
        if (this.cfg.syncTooltip) {
            this.syncTitle(b)
        }
        this.triggers.data("primefaces-overlay-target", true).find("*").data("primefaces-overlay-target", true);
        if (!this.disabled) {
            this.bindEvents();
            this.bindConstantEvents();
            this.appendPanel()
        }
        this.input.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        if (PrimeFaces.env.touch) {
            this.focusInput.attr("readonly", true)
        }
        for (var a = 0; a < this.items.size(); a++) {
            this.items.eq(a).attr("id", this.id + "_" + a)
        }
        var f = c.attr("id");
        this.focusInput.attr("aria-autocomplete", "list").attr("aria-owns", this.itemsContainer.attr("id")).attr("aria-activedescendant", f).attr("aria-describedby", f).attr("aria-disabled", this.disabled);
        this.itemsContainer.attr("aria-activedescendant", f)
    },
    refresh: function(a) {
        this.panelWidthAdjusted = false;
        this._super(a)
    },
    appendPanel: function() {
        var a = this.cfg.appendTo ? PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.appendTo) : $(document.body);
        if (!a.is(this.jq)) {
            a.children(this.panelId).remove();
            this.panel.appendTo(a)
        }
    },
    alignPanelWidth: function() {
        if (!this.panelWidthAdjusted) {
            var a = this.jq.outerWidth();
            if (this.panel.outerWidth() < a) {
                this.panel.width(a)
            }
            this.panelWidthAdjusted = true
        }
    },
    bindEvents: function() {
        var a = this;
        if (PrimeFaces.env.browser.webkit) {
            this.input.on("focus", function() {
                setTimeout(function() {
                    a.focusInput.trigger("focus.ui-selectonemenu")
                }, 2)
            })
        }
        this.items.filter(":not(.ui-state-disabled)").on("mouseover.selectonemenu", function() {
            var b = $(this);
            if (!b.hasClass("ui-state-highlight")) {
                $(this).addClass("ui-state-hover")
            }
        }).on("mouseout.selectonemenu", function() {
            $(this).removeClass("ui-state-hover")
        }).on("click.selectonemenu", function() {
            a.selectItem($(this));
            a.changeAriaValue($(this))
        });
        this.triggers.mouseenter(function() {
            if (!a.jq.hasClass("ui-state-focus")) {
                a.jq.addClass("ui-state-hover");
                a.menuIcon.addClass("ui-state-hover")
            }
        }).mouseleave(function() {
            a.jq.removeClass("ui-state-hover");
            a.menuIcon.removeClass("ui-state-hover")
        }).click(function(b) {
            if (a.panel.is(":hidden")) {
                a.show()
            } else {
                a.hide();
                a.revert();
                a.changeAriaValue(a.getActiveItem())
            }
            a.jq.removeClass("ui-state-hover");
            a.menuIcon.removeClass("ui-state-hover");
            a.focusInput.trigger("focus.ui-selectonemenu");
            b.preventDefault()
        });
        this.focusInput.on("focus.ui-selectonemenu", function() {
            a.jq.addClass("ui-state-focus");
            a.menuIcon.addClass("ui-state-focus")
        }).on("blur.ui-selectonemenu", function() {
            a.jq.removeClass("ui-state-focus");
            a.menuIcon.removeClass("ui-state-focus")
        });
        if (this.cfg.editable) {
            this.label.change(function() {
                a.triggerChange(true);
                a.customInput = true;
                a.customInputVal = $(this).val();
                a.items.filter(".ui-state-active").removeClass("ui-state-active");
                a.items.eq(0).addClass("ui-state-active")
            })
        }
        this.bindKeyEvents();
        if (this.cfg.filter) {
            this.cfg.initialHeight = this.itemsWrapper.height();
            this.setupFilterMatcher();
            this.filterInput = this.panel.find("> div.ui-selectonemenu-filter-container > input.ui-selectonemenu-filter");
            PrimeFaces.skinInput(this.filterInput);
            this.bindFilterEvents()
        }
    },
    bindConstantEvents: function() {
        var b = this,
                a = "mousedown." + this.id;
        $(document).off(a).on(a, function(c) {
            if (b.panel.is(":hidden")) {
                return
            }
            var d = b.panel.offset();
            if (c.target === b.label.get(0) || c.target === b.menuIcon.get(0) || c.target === b.menuIcon.children().get(0)) {
                return
            }
            if (c.pageX < d.left || c.pageX > d.left + b.panel.width() || c.pageY < d.top || c.pageY > d.top + b.panel.height()) {
                b.hide();
                b.revert();
                b.changeAriaValue(b.getActiveItem())
            }
        });
        this.resizeNS = "resize." + this.id;
        this.unbindResize();
        this.bindResize()
    },
    bindResize: function() {
        var a = this;
        $(window).bind(this.resizeNS, function(b) {
            if (a.panel.is(":visible")) {
                a.alignPanel()
            }
        })
    },
    unbindResize: function() {
        $(window).unbind(this.resizeNS)
    },
    unbindEvents: function() {
        this.items.off();
        this.triggers.off();
        this.input.off();
        this.focusInput.off();
        this.label.off()
    },
    revert: function() {
        if (this.cfg.editable && this.customInput) {
            this.setLabel(this.customInputVal);
            this.items.filter(".ui-state-active").removeClass("ui-state-active");
            this.items.eq(0).addClass("ui-state-active")
        } else {
            this.highlightItem(this.items.eq(this.preShowValue.index()))
        }
    },
    highlightItem: function(a) {
        this.items.filter(".ui-state-highlight").removeClass("ui-state-highlight");
        if (a.length > 0) {
            a.addClass("ui-state-highlight");
            this.setLabel(a.data("label"))
        }
    },
    triggerChange: function(a) {
        this.changed = false;
        this.input.trigger("change");
        if (!a) {
            this.value = this.options.filter(":selected").val()
        }
    },
    triggerItemSelect: function() {
        if (this.cfg.behaviors) {
            var a = this.cfg.behaviors.itemSelect;
            if (a) {
                a.call(this)
            }
        }
    },
    selectItem: function(d, f) {
        var c = this.options.eq(this.resolveItemIndex(d)),
                b = this.options.filter(":selected"),
                e = c.val() == b.val(),
                a = null;
        if (this.cfg.editable) {
            a = (!e) || (c.text() != this.label.val())
        } else {
            a = !e
        }
        if (a) {
            this.highlightItem(d);
            this.input.val(c.val());
            this.triggerChange();
            if (this.cfg.editable) {
                this.customInput = false
            }
            if (this.cfg.syncTooltip) {
                this.syncTitle(c)
            }
        }
        if (!f) {
            this.focusInput.focus();
            this.triggerItemSelect()
        }
        if (this.panel.is(":visible")) {
            this.hide()
        }
    },
    syncTitle: function(b) {
        var a = this.items.eq(b.index()).attr("title");
        if (a) {
            this.jq.attr("title", this.items.eq(b.index()).attr("title"))
        } else {
            this.jq.removeAttr("title")
        }
    },
    resolveItemIndex: function(a) {
        if (this.optGroupsSize === 0) {
            return a.index()
        } else {
            return a.index() - a.prevAll("li.ui-selectonemenu-item-group").length
        }
    },
    bindKeyEvents: function() {
        var a = this;
        this.focusInput.on("keydown.ui-selectonemenu", function(d) {
            var c = $.ui.keyCode,
                    b = d.which;
            switch (b) {
                case c.UP:
                case c.LEFT:
                    a.highlightPrev(d);
                    break;
                case c.DOWN:
                case c.RIGHT:
                    a.highlightNext(d);
                    break;
                case c.ENTER:
                case c.NUMPAD_ENTER:
                    a.handleEnterKey(d);
                    break;
                case c.TAB:
                    a.handleTabKey();
                    break;
                case c.ESCAPE:
                    a.handleEscapeKey(d);
                    break;
                case c.SPACE:
                    a.handleSpaceKey(d);
                    break
            }
        }).on("keyup.ui-selectonemenu", function(f) {
            var d = $.ui.keyCode,
                    b = f.which;
            switch (b) {
                case d.UP:
                case d.LEFT:
                case d.DOWN:
                case d.RIGHT:
                case d.ENTER:
                case d.NUMPAD_ENTER:
                case d.TAB:
                case d.ESCAPE:
                case d.SPACE:
                case d.HOME:
                case d.PAGE_DOWN:
                case d.PAGE_UP:
                case d.END:
                case d.DELETE:
                case 16:
                case 17:
                case 18:
                case 91:
                case 92:
                case 93:
                case 20:
                    break;
                default:
                    var h = $(this).val(),
                            i = null,
                            g = f.metaKey || f.ctrlKey || f.shiftKey;
                    if (!g) {
                        clearTimeout(a.searchTimer);
                        i = a.options.filter(function() {
                            return $(this).text().toLowerCase().indexOf(h.toLowerCase()) === 0
                        });
                        if (i.length) {
                            var c = a.items.eq(i.index());
                            if (a.panel.is(":hidden")) {
                                a.selectItem(c)
                            } else {
                                a.highlightItem(c);
                                PrimeFaces.scrollInView(a.itemsWrapper, c)
                            }
                        }
                        a.searchTimer = setTimeout(function() {
                            a.focusInput.val("")
                        }, 1000)
                    }
                    break
            }
        })
    },
    bindFilterEvents: function() {
        var a = this;
        this.filterInput.on("keyup.ui-selectonemenu", function(d) {
            var c = $.ui.keyCode,
                    b = d.which;
            switch (b) {
                case c.UP:
                case c.LEFT:
                case c.DOWN:
                case c.RIGHT:
                case c.ENTER:
                case c.NUMPAD_ENTER:
                case c.TAB:
                case c.ESCAPE:
                case c.SPACE:
                case c.HOME:
                case c.PAGE_DOWN:
                case c.PAGE_UP:
                case c.END:
                case c.DELETE:
                case 16:
                case 17:
                case 18:
                case 91:
                case 92:
                case 93:
                case 20:
                    break;
                default:
                    var f = d.metaKey || d.ctrlKey;
                    if (!f) {
                        a.filter($(this).val())
                    }
                    break
            }
        }).on("keydown.ui-selectonemenu", function(d) {
            var c = $.ui.keyCode,
                    b = d.which;
            switch (b) {
                case c.UP:
                    a.highlightPrev(d);
                    break;
                case c.DOWN:
                    a.highlightNext(d);
                    break;
                case c.ENTER:
                case c.NUMPAD_ENTER:
                    a.handleEnterKey(d);
                    break;
                case c.TAB:
                    a.handleTabKey();
                    break;
                case c.ESCAPE:
                    a.handleEscapeKey(d);
                    break;
                case c.SPACE:
                    a.handleSpaceKey(d);
                    break;
                default:
                    break
            }
        })
    },
    highlightNext: function(c) {
        var a = this.getActiveItem(),
                b = this.panel.is(":hidden") ? a.nextAll(":not(.ui-state-disabled,.ui-selectonemenu-item-group):first") : a.nextAll(":not(.ui-state-disabled,.ui-selectonemenu-item-group):visible:first");
        if (b.length === 1) {
            if (this.panel.is(":hidden")) {
                if (c.altKey) {
                    this.show()
                } else {
                    this.selectItem(b)
                }
            } else {
                this.highlightItem(b);
                PrimeFaces.scrollInView(this.itemsWrapper, b)
            }
            this.changeAriaValue(b)
        }
        c.preventDefault()
    },
    highlightPrev: function(c) {
        var a = this.getActiveItem(),
                b = this.panel.is(":hidden") ? a.prevAll(":not(.ui-state-disabled,.ui-selectonemenu-item-group):first") : a.prevAll(":not(.ui-state-disabled,.ui-selectonemenu-item-group):visible:first");
        if (b.length === 1) {
            if (this.panel.is(":hidden")) {
                this.selectItem(b)
            } else {
                this.highlightItem(b);
                PrimeFaces.scrollInView(this.itemsWrapper, b)
            }
            this.changeAriaValue(b)
        }
        c.preventDefault()
    },
    handleEnterKey: function(a) {
        if (this.panel.is(":visible")) {
            this.selectItem(this.getActiveItem())
        }
        a.preventDefault();
        a.stopPropagation()
    },
    handleSpaceKey: function(a) {
        var b = $(a.target);
        if (b.is("input") && b.hasClass("ui-selectonemenu-filter")) {
            return
        }
        if (this.panel.is(":hidden")) {
            this.show()
        } else {
            this.hide();
            this.revert();
            this.changeAriaValue(this.getActiveItem())
        }
        a.preventDefault()
    },
    handleEscapeKey: function(a) {
        if (this.panel.is(":visible")) {
            this.revert();
            this.hide()
        }
        a.preventDefault()
    },
    handleTabKey: function() {
        if (this.panel.is(":visible")) {
            this.selectItem(this.getActiveItem())
        }
    },
    show: function() {
        var a = this;
        this.alignPanel();
        this.panel.css("z-index", ++PrimeFaces.zindex);
        if ($.browser.msie && /^[6,7]\.[0-9]+/.test($.browser.version)) {
            this.panel.parent().css("z-index", PrimeFaces.zindex - 1)
        }
        if (this.cfg.effect !== "none") {
            this.panel.show(this.cfg.effect, {}, this.cfg.effectSpeed, function() {
                PrimeFaces.scrollInView(a.itemsWrapper, a.getActiveItem());
                if (a.cfg.filter) {
                    a.focusFilter()
                }
            })
        } else {
            this.panel.show();
            PrimeFaces.scrollInView(this.itemsWrapper, this.getActiveItem());
            if (a.cfg.filter) {
                this.focusFilter(10)
            }
        }
        this.preShowValue = this.options.filter(":selected");
        this.focusInput.attr("aria-expanded", true)
    },
    hide: function() {
        if ($.browser.msie && /^[6,7]\.[0-9]+/.test($.browser.version)) {
            this.panel.parent().css("z-index", "")
        }
        this.panel.css("z-index", "").hide();
        this.focusInput.attr("aria-expanded", false)
    },
    focus: function() {
        this.focusInput.focus()
    },
    focusFilter: function(a) {
        if (a) {
            var b = this;
            setTimeout(function() {
                b.focusFilter()
            }, a)
        } else {
            this.filterInput.focus()
        }
    },
    blur: function() {
        this.focusInput.blur()
    },
    disable: function() {
        if (!this.disabled) {
            this.disabled = true;
            this.jq.addClass("ui-state-disabled");
            this.input.attr("disabled", "disabled");
            if (this.cfg.editable) {
                this.label.attr("disabled", "disabled")
            }
            this.unbindEvents()
        }
    },
    enable: function() {
        if (this.disabled) {
            this.disabled = false;
            this.jq.removeClass("ui-state-disabled");
            this.input.removeAttr("disabled");
            if (this.cfg.editable) {
                this.label.removeAttr("disabled")
            }
            this.bindEvents()
        }
    },
    alignPanel: function() {
        this.alignPanelWidth();
        if (this.panel.parent().is(this.jq)) {
            this.panel.css({
                left: 0,
                top: this.jq.innerHeight()
            })
        } else {
            this.panel.css({
                left: "",
                top: ""
            }).position({
                my: "left top",
                at: "left bottom",
                of: this.jq,
                collision: "flipfit"
            })
        }
    },
    setLabel: function(b) {
        var a = this.getLabelToDisplay(b);
        if (this.cfg.editable) {
            if (b === "&nbsp;") {
                this.label.val("")
            } else {
                this.label.val(a)
            }
        } else {
            if (b === "&nbsp;") {
                this.label.html("&nbsp;")
            } else {
                this.label.text(a)
            }
        }
    },
    selectValue: function(b) {
        var a = this.options.filter('[value="' + b + '"]');
        this.selectItem(this.items.eq(a.index()), true)
    },
    getActiveItem: function() {
        return this.items.filter(".ui-state-highlight")
    },
    setupFilterMatcher: function() {
        this.cfg.filterMatchMode = this.cfg.filterMatchMode || "startsWith";
        this.filterMatchers = {
            startsWith: this.startsWithFilter,
            contains: this.containsFilter,
            endsWith: this.endsWithFilter,
            custom: this.cfg.filterFunction
        };
        this.filterMatcher = this.filterMatchers[this.cfg.filterMatchMode]
    },
    startsWithFilter: function(b, a) {
        return b.indexOf(a) === 0
    },
    containsFilter: function(b, a) {
        return b.indexOf(a) !== -1
    },
    endsWithFilter: function(b, a) {
        return b.indexOf(a, b.length - a.length) !== -1
    },
    filter: function(f) {
        this.cfg.initialHeight = this.cfg.initialHeight || this.itemsWrapper.height();
        var e = this.cfg.caseSensitive ? $.trim(f) : $.trim(f).toLowerCase();
        if (e === "") {
            this.items.filter(":hidden").show();
            this.itemsContainer.children(".ui-selectonemenu-item-group").show()
        } else {
            for (var a = 0; a < this.options.length; a++) {
                var b = this.options.eq(a),
                        l = this.cfg.caseSensitive ? b.text() : b.text().toLowerCase(),
                        j = this.items.eq(a);
                if (this.filterMatcher(l, e)) {
                    j.show()
                } else {
                    j.hide()
                }
            }
            var k = this.itemsContainer.children(".ui-selectonemenu-item-group");
            for (var c = 0; c < k.length; c++) {
                var h = k.eq(c);
                if (c === (k.length - 1)) {
                    if (h.nextAll().filter(":visible").length === 0) {
                        h.hide()
                    } else {
                        h.show()
                    }
                } else {
                    if (h.nextUntil(".ui-selectonemenu-item-group").filter(":visible").length === 0) {
                        h.hide()
                    } else {
                        h.show()
                    }
                }
            }
        }
        var d = this.items.filter(":visible:first");
        if (d.length) {
            this.highlightItem(d)
        }
        if (this.itemsContainer.height() < this.cfg.initialHeight) {
            this.itemsWrapper.css("height", "auto")
        } else {
            this.itemsWrapper.height(this.cfg.initialHeight)
        }
        this.alignPanel()
    },
    getSelectedValue: function() {
        return this.input.val()
    },
    getSelectedLabel: function() {
        return this.options.filter(":selected").text()
    },
    getLabelToDisplay: function(a) {
        if (this.cfg.labelTemplate && a !== "&nbsp;") {
            return this.cfg.labelTemplate.replace("{0}", a)
        }
        return a
    },
    changeAriaValue: function(a) {
        var b = a.attr("id");
        this.focusInput.attr("aria-activedescendant", b).attr("aria-describedby", b);
        this.itemsContainer.attr("aria-activedescendant", b)
    }
    
    
});